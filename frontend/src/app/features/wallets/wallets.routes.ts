import { Routes } from '@angular/router';

export const walletsRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./wallets-page/wallets-page.component').then(m => m.WalletsPageComponent),
  },
];
