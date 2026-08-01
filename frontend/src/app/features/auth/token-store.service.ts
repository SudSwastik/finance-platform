import { Injectable } from '@angular/core';

interface StoredTokens {
  accessToken: string;
  idToken: string;
  refreshToken: string;
  /** epoch millis */
  expiresAt: number;
}

const STORAGE_KEY = 'ledgerly.auth.tokens';

@Injectable({ providedIn: 'root' })
export class TokenStoreService {
  setTokens(accessToken: string, idToken: string, refreshToken: string, expiresInSeconds: number): void {
    const tokens: StoredTokens = {
      accessToken,
      idToken,
      refreshToken,
      expiresAt: Date.now() + expiresInSeconds * 1000,
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(tokens));
  }

  /** Access token only replaced — refresh token is unchanged across a refresh call. */
  setAccessToken(accessToken: string, idToken: string, expiresInSeconds: number): void {
    const existing = this.read();
    if (!existing) return;
    this.setTokens(accessToken, idToken, existing.refreshToken, expiresInSeconds);
  }

  getAccessToken(): string | null {
    return this.read()?.accessToken ?? null;
  }

  getRefreshToken(): string | null {
    return this.read()?.refreshToken ?? null;
  }

  /** True once the access token is expired, or within 30s of expiring. */
  isAccessTokenExpired(): boolean {
    const tokens = this.read();
    if (!tokens) return true;
    return Date.now() >= tokens.expiresAt - 30_000;
  }

  hasSession(): boolean {
    return this.read() !== null;
  }

  clear(): void {
    localStorage.removeItem(STORAGE_KEY);
  }

  private read(): StoredTokens | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as StoredTokens;
    } catch {
      return null;
    }
  }
}
