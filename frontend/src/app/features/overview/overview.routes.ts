import { Routes } from '@angular/router';

export const overviewRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./overview.page').then(m => m.OverviewPageComponent),
  },
];
