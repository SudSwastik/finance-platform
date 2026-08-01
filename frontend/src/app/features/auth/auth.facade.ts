import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import {
  CognitoIdentityProviderClient,
  ConfirmSignUpCommand,
  InitiateAuthCommand,
  ResendConfirmationCodeCommand,
  SignUpCommand,
} from '@aws-sdk/client-cognito-identity-provider';
import { environment } from '../../../environments/environment';
import { TokenStoreService } from './token-store.service';

export interface LoginPayload {
  email: string;
  password: string;
  rememberMe: boolean;
}

export interface RegisterPayload {
  name: string;
  email: string;
  password: string;
}

/**
 * Talks to Cognito directly (SignUp/InitiateAuth) rather than redirecting to a Hosted
 * UI — this app has its own branded auth pages. tenant/scope aren't in the token; see
 * ProvisionMyProfileCommandHandler on identity-service for where they get resolved.
 */
@Injectable({ providedIn: 'root' })
export class AuthFacade {
  private readonly http = inject(HttpClient);
  private readonly tokenStore = inject(TokenStoreService);
  private readonly cognito = new CognitoIdentityProviderClient({ region: environment.cognito.region });

  // Accumulated across signup + onboarding, sent to identity-service in finishOnboarding().
  // The password only lives here long enough to auto-login right after ConfirmSignUp.
  private pendingName = '';
  private pendingEmail = '';
  private pendingPassword = '';
  private pendingAccountType: 'personal' | 'business' = 'personal';

  async register(payload: RegisterPayload): Promise<void> {
    this.pendingName = payload.name;
    this.pendingEmail = payload.email;
    this.pendingPassword = payload.password;

    await this.cognito.send(new SignUpCommand({
      ClientId: environment.cognito.clientId,
      Username: payload.email,
      Password: payload.password,
      UserAttributes: [
        { Name: 'email', Value: payload.email },
        { Name: 'name', Value: payload.name },
      ],
    }));
  }

  async confirmSignUp(code: string): Promise<void> {
    await this.cognito.send(new ConfirmSignUpCommand({
      ClientId: environment.cognito.clientId,
      Username: this.pendingEmail,
      ConfirmationCode: code,
    }));

    // Confirmed accounts aren't automatically signed in — log in now so the onboarding
    // wizard has a valid access token to call identity-service with.
    await this.login({ email: this.pendingEmail, password: this.pendingPassword, rememberMe: false });
    this.pendingPassword = '';
  }

  async resendVerificationEmail(): Promise<void> {
    await this.cognito.send(new ResendConfirmationCodeCommand({
      ClientId: environment.cognito.clientId,
      Username: this.pendingEmail,
    }));
  }

  async login(payload: LoginPayload): Promise<void> {
    const result = await this.cognito.send(new InitiateAuthCommand({
      ClientId: environment.cognito.clientId,
      AuthFlow: 'USER_PASSWORD_AUTH',
      AuthParameters: { USERNAME: payload.email, PASSWORD: payload.password },
    }));

    const auth = result.AuthenticationResult;
    if (!auth?.AccessToken || !auth.IdToken || !auth.RefreshToken || !auth.ExpiresIn) {
      throw new Error('Cognito did not return a complete set of tokens');
    }
    this.tokenStore.setTokens(auth.AccessToken, auth.IdToken, auth.RefreshToken, auth.ExpiresIn);
  }

  forgotPassword(email: string): void {
    console.log('[AuthFacade] forgotPassword stub', email);
  }

  submitAccountType(type: 'personal' | 'business'): void {
    this.pendingAccountType = type;
  }

  submitCountry(_countryCode: string): void {
    // Not persisted yet — identity.users has no country column (see docs/REFERENCE.md's
    // data model). Collected here for when a real use for it lands.
  }

  submitPhone(_phone: string): void {
    // Same as submitCountry — collected, not persisted; real phone verification is deferred.
  }

  /** Creates the Tenant + User row for this sub if it doesn't already exist, then this
   *  onboarding session's accumulated data is done being useful. Idempotent on the backend. */
  async finishOnboarding(): Promise<void> {
    const accessToken = this.tokenStore.getAccessToken();
    if (!accessToken) {
      throw new Error('Not signed in — cannot provision a profile without a session');
    }
    await firstValueFrom(this.http.post(
      '/api/v1/identity/me',
      { name: this.pendingName, email: this.pendingEmail, accountType: this.pendingAccountType },
      { headers: new HttpHeaders({ Authorization: `Bearer ${accessToken}` }) },
    ));
  }
}
