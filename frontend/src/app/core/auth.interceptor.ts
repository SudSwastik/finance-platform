import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { from, switchMap } from 'rxjs';
import { AuthFacade } from '../features/auth/auth.facade';
import { TokenStoreService } from '../features/auth/token-store.service';

/** Attaches a Bearer token to same-origin API calls, refreshing it first if it's
 *  expired or close to it. Cognito's own SDK calls go straight to AWS via fetch,
 *  never through Angular's HttpClient, so they never hit this interceptor. */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (!req.url.startsWith('/api/')) {
    return next(req);
  }

  const tokenStore = inject(TokenStoreService);
  if (!tokenStore.hasSession()) {
    return next(req);
  }

  if (!tokenStore.isAccessTokenExpired()) {
    const accessToken = tokenStore.getAccessToken();
    return next(req.clone({ setHeaders: { Authorization: `Bearer ${accessToken}` } }));
  }

  const facade = inject(AuthFacade);
  return from(facade.refreshSession()).pipe(
    switchMap(() => {
      const accessToken = tokenStore.getAccessToken();
      return next(accessToken ? req.clone({ setHeaders: { Authorization: `Bearer ${accessToken}` } }) : req);
    }),
  );
};
