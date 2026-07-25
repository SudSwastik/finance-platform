import { Routes } from '@angular/router';

export const goalsRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./goals-page/goals-page.component').then(m => m.GoalsPageComponent),
  },
];
