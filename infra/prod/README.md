# Deploy on Ubuntu EC2 with Nginx

Nginx runs on the host, and this project gets a dedicated domain/subdomain such as `finance.example.com`.
Other projects can keep their own Nginx server blocks.

Set the repository path once in your deployment shell, replacing the example
folder with your actual checkout location:

```bash
export PROJECT_DIR="/your-project-folder"
```

Commands below use `$PROJECT_DIR`. Set it again when opening a new SSH session.

The browser connects to Nginx on HTTPS. Nginx serves the Angular build and proxies
API calls to Docker services bound to localhost. PostgreSQL is private to Docker;
LocalStack is excluded. Cognito runs in AWS.

## 1. Prerequisites

Install Git, Java 21 JDK, Maven 3.9+, Node.js 22 with npm, Docker Engine with the
Compose plugin, Nginx, and Certbot with its Nginx plugin. Ensure Docker starts on
boot. Commands below assume your deployment user can run Docker.

Use [Docker's Ubuntu installation instructions](https://docs.docker.com/engine/install/ubuntu/).
If Java, Maven, and Node are already available, Ubuntu's Nginx/Certbot packages are:

```bash
sudo apt update
sudo apt install nginx certbot python3-certbot-nginx
sudo systemctl enable --now nginx docker
```

In the EC2 security group, allow inbound 80/443 for the website and restrict SSH
22 to your IP. Leave 5432, 4566, 4200, and 8081–8086 closed to the internet.
Point the project's DNS A record to the instance's Elastic IP. Ensure the instance
has outbound internet access for image downloads, dependencies, and Cognito.

## 2. Configure the application

Ensure the deployed checkout contains the latest fixes, then:

```bash
cd "$PROJECT_DIR"
cp -n infra/prod/.env.example infra/prod/.env
chmod 600 infra/prod/.env
nano infra/prod/.env
```

Set a long random `POSTGRES_PASSWORD` and the real `COGNITO_ISSUER_URI`.
Keep `COMPOSE_PROJECT_NAME=finance-platform` stable: it isolates this project's
containers, network, and database volume from other projects.

Confirm the region, pool ID, and public client ID in
`frontend/src/environments/environment.prod.ts` match that issuer. Production
builds use this environment file; environment values are embedded at build time.
Changing `.env` alone does not change the frontend Cognito configuration.

## 3. Build and start

The existing service Dockerfiles copy prebuilt JARs, so build the backend first:

```bash
cd "$PROJECT_DIR/backend"
mvn clean package

cd "$PROJECT_DIR/frontend"
npm ci
npm run build

cd "$PROJECT_DIR"
docker compose --env-file infra/prod/.env -f infra/prod/docker-compose.yml config --quiet
docker compose --env-file infra/prod/.env -f infra/prod/docker-compose.yml up -d --build
docker compose --env-file infra/prod/.env -f infra/prod/docker-compose.yml ps
curl --fail http://127.0.0.1:8081/api/v1/health
```

Check for port conflicts with other projects before starting. This stack uses
host loopback ports 8081–8086. If those are occupied, change the host side of the
production Compose port mappings and the matching Nginx `proxy_pass` targets.
Container ports and internal service URLs can stay unchanged.

The production database is a new named volume; your laptop's data is not copied.
Existing Flyway migrations include development seed users/data; real Cognito users
have separate ownership and start without linked accounts. Review those migrations
before storing real financial data. Back up PostgreSQL separately: the named
volume survives container replacement but is not a backup. Changing the password
in `.env` does not change a password in an already-initialized database.

## 4. Add this project's Nginx site

```bash
sudo cp "$PROJECT_DIR/infra/prod/nginx.conf" /etc/nginx/sites-available/finance-platform
sudo nano /etc/nginx/sites-available/finance-platform
```

Replace `finance.example.com` with your domain. Set the Nginx `root` to the actual
absolute path of your frontend build, for example:

```nginx
root /srv/your-project-folder/frontend/dist/frontend/browser;
```

Nginx does not expand the shell variable `$PROJECT_DIR`; enter the actual path.
The `finance-platform` Nginx site filename is just a label and can stay the same
regardless of the repository folder name. Give Nginx's `www-data` user read/traverse access to the frontend
build and its parent directories; keep `.env` private. Do not change permissions
recursively across `/srv`, because it contains other projects.

```bash
sudo ln -s /etc/nginx/sites-available/finance-platform /etc/nginx/sites-enabled/finance-platform
sudo nginx -t
sudo systemctl reload nginx
sudo certbot --nginx -d finance.example.com
```

Replace the domain in the Certbot command too. Keep existing project sites enabled.
Do not run `ng serve` on EC2: Nginx serves
`frontend/dist/frontend/browser/index.html` and supports direct Angular routes such
as `/wallets`. API routes preserve the full `/api/v1/...` path and forward bearer
tokens. Open the HTTPS domain, register/sign in, then check Overview and Wallets.

## Updates and troubleshooting

After pulling a reviewed commit, repeat the backend/frontend builds and Compose
`up -d --build` command. Nginx reload is only needed when its configuration changes.
Schedule a brief maintenance window for this simple in-place deployment; it does
not provide atomic releases or zero downtime.

```bash
cd "$PROJECT_DIR"
docker compose --env-file infra/prod/.env -f infra/prod/docker-compose.yml logs --tail=100 dashboard-bff
sudo tail -n 100 /var/log/nginx/finance-platform.error.log
curl --fail https://finance.example.com/api/v1/health
```

An unauthenticated call to `/api/v1/dashboard/overview` should return 401.
Never run Compose `down -v` to update the app: it deletes this stack's database
volume. Automatic container restarts use `unless-stopped`.
