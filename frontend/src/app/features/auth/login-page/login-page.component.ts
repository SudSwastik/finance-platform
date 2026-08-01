import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthFacade } from '../auth.facade';
import { AuthShellComponent } from '../auth-shell/auth-shell.component';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [FormsModule, RouterLink, AuthShellComponent],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
})
export class LoginPageComponent {
  private readonly facade = inject(AuthFacade);
  private readonly router = inject(Router);

  email = '';
  password = '';
  rememberMe = false;
  showPassword = false;
  submitting = false;
  errorMessage = '';

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  async onSubmit(): Promise<void> {
    this.errorMessage = '';
    this.submitting = true;
    try {
      await this.facade.login({ email: this.email, password: this.password, rememberMe: this.rememberMe });
      this.router.navigate(['/overview']);
    } catch (err) {
      this.errorMessage = err instanceof Error ? err.message : 'Login failed. Check your email and password.';
    } finally {
      this.submitting = false;
    }
  }
}
