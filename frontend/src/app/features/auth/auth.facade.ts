import { Injectable } from '@angular/core';

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

@Injectable({ providedIn: 'root' })
export class AuthFacade {
  login(payload: LoginPayload): void {
    console.log('[AuthFacade] login stub', payload);
  }

  register(payload: RegisterPayload): void {
    console.log('[AuthFacade] register stub', payload);
  }

  resendVerificationEmail(): void {
    console.log('[AuthFacade] resendVerificationEmail stub');
  }

  forgotPassword(email: string): void {
    console.log('[AuthFacade] forgotPassword stub', email);
  }

  submitAccountType(type: 'personal' | 'business'): void {
    console.log('[AuthFacade] submitAccountType stub', type);
  }

  submitCountry(countryCode: string): void {
    console.log('[AuthFacade] submitCountry stub', countryCode);
  }

  submitPhone(phone: string): void {
    console.log('[AuthFacade] submitPhone stub', phone);
  }

  verify2fa(code: string): void {
    console.log('[AuthFacade] verify2fa stub', code);
  }
}
