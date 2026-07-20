import { Routes } from '@angular/router';

export const subscriptionRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./subscriptions-page/subscriptions-page.component').then(m => m.SubscriptionsPageComponent),
  },
];
