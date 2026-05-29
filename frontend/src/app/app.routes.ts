import { Routes } from '@angular/router';
import { AppShellComponent } from './shell/app-shell/app-shell.component';
import { PlaceholderPageComponent } from './shared/ui/placeholder-page/placeholder-page.component';

export const routes: Routes = [
  {
    path: '',
    component: AppShellComponent,
    children: [
      { path: '', redirectTo: 'overview', pathMatch: 'full' },
      {
        path: 'overview',
        loadChildren: () =>
          import('./features/overview/overview.routes').then(m => m.overviewRoutes),
      },
      { path: 'wallets',       component: PlaceholderPageComponent, data: { title: 'Wallets & Banks' } },
      { path: 'activity',      component: PlaceholderPageComponent, data: { title: 'Activity Log' } },
      { path: 'movement',      component: PlaceholderPageComponent, data: { title: 'Money Movement' } },
      { path: 'insights',      component: PlaceholderPageComponent, data: { title: 'Insights' } },
      { path: 'spending-plan', component: PlaceholderPageComponent, data: { title: 'Spending Plan' } },
      { path: 'subscriptions', component: PlaceholderPageComponent, data: { title: 'Subscriptions' } },
      { path: 'goals',         component: PlaceholderPageComponent, data: { title: 'Savings Goals' } },
      { path: 'portfolio',     component: PlaceholderPageComponent, data: { title: 'Portfolio' } },
      { path: 'tips',          component: PlaceholderPageComponent, data: { title: 'Smart Tips' } },
      { path: 'settings',      component: PlaceholderPageComponent, data: { title: 'Settings' } },
    ],
  },
  { path: '**', redirectTo: '' },
];
