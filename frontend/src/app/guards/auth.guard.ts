import { CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = () => {
  // Phase 6: redirect to /login if not authenticated
  return true;
};
