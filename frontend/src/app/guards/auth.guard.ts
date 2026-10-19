import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenStoreService } from '../features/auth/token-store.service';

export const authGuard: CanActivateFn = () => {
  const tokenStore = inject(TokenStoreService);
  if (tokenStore.hasSession()) {
    return true;
  }
  const router = inject(Router);
  return router.parseUrl('/login');
};
