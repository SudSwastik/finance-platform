import { Routes } from '@angular/router';
import { DashboardLayoutComponent } from './layout/dashboard-layout/dashboard-layout.component';
import { PlaceholderPageComponent } from './shared/ui/placeholder-page/placeholder-page.component';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login-page/login-page.component').then(m => m.LoginPageComponent),
  },
  {
    path: 'register',
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () =>
          import('./features/auth/register-page/register-page.component').then(m => m.RegisterPageComponent),
      },
      {
        path: 'check-email',
        loadComponent: () =>
          import('./features/auth/check-email-page/check-email-page.component').then(m => m.CheckEmailPageComponent),
      },
      {
        path: 'email-verified',
        loadComponent: () =>
          import('./features/auth/email-verified-page/email-verified-page.component').then(m => m.EmailVerifiedPageComponent),
      },
      {
        path: 'onboarding',
        loadComponent: () =>
          import('./features/auth/onboarding-shell/onboarding-shell.component').then(m => m.OnboardingShellComponent),
        children: [
          { path: '', redirectTo: 'account-type', pathMatch: 'full' },
          {
            path: 'account-type',
            loadComponent: () =>
              import('./features/auth/onboarding/account-type-page/account-type-page.component').then(m => m.AccountTypePageComponent),
            data: { step: 2 },
          },
          {
            path: 'country',
            loadComponent: () =>
              import('./features/auth/onboarding/select-country-page/select-country-page.component').then(m => m.SelectCountryPageComponent),
            data: { step: 3 },
          },
          {
            path: 'phone',
            loadComponent: () =>
              import('./features/auth/onboarding/phone-number-page/phone-number-page.component').then(m => m.PhoneNumberPageComponent),
            data: { step: 4 },
          },
          {
            path: 'verify-2fa',
            loadComponent: () =>
              import('./features/auth/onboarding/verify-2fa-page/verify-2fa-page.component').then(m => m.Verify2faPageComponent),
            data: { step: 4 },
          },
        ],
      },
    ],
  },
  {
    path: 'forgot-password',
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./features/auth/forgot-password-page/forgot-password-page.component').then(m => m.ForgotPasswordPageComponent),
      },
      {
        path: 'success',
        loadComponent: () =>
          import('./features/auth/forgot-password-success-page/forgot-password-success-page.component').then(m => m.ForgotPasswordSuccessPageComponent),
      },
    ],
  },
  {
    path: '',
    component: DashboardLayoutComponent,
    children: [
      { path: '', redirectTo: 'overview', pathMatch: 'full' },
      {
        path: 'overview',
        loadChildren: () =>
          import('./features/overview/overview.routes').then(m => m.overviewRoutes),
      },
      {
        path: 'transactions',
        loadChildren: () =>
          import('./features/transactions/transactions.routes').then(m => m.transactionRoutes),
      },
      { path: 'wallets',       component: PlaceholderPageComponent, data: { title: 'Wallets & Banks' } },
      { path: 'movement',      component: PlaceholderPageComponent, data: { title: 'Money Movement' } },
      { path: 'insights',      component: PlaceholderPageComponent, data: { title: 'Insights' } },
      { path: 'spending-plan', component: PlaceholderPageComponent, data: { title: 'Spending Plan' } },
      { path: 'subscriptions', component: PlaceholderPageComponent, data: { title: 'Subscriptions' } },
      { path: 'portfolio',     component: PlaceholderPageComponent, data: { title: 'Portfolio' } },
      { path: 'tips',          component: PlaceholderPageComponent, data: { title: 'Smart Tips' } },
      { path: 'settings',      component: PlaceholderPageComponent, data: { title: 'Settings' } },
    ],
  },
  { path: '**', redirectTo: '' },
];
