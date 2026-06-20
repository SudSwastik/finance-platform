import { Routes } from '@angular/router';

export const transactionRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./transactions-page/transactions-page.component').then(m => m.TransactionsPageComponent),
  },
];
