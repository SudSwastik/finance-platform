import { Provider } from '@angular/core';
import { environment } from '../../environments/environment';
import { OverviewRepository } from './overview/overview.repository';
import { OverviewMockRepository } from './overview/overview-mock.repository';

export function provideDataAccess(): Provider[] {
  if (environment.useMockData) {
    return [
      { provide: OverviewRepository, useClass: OverviewMockRepository },
    ];
  }
  // Phase 5: swap in HTTP repositories
  return [
    { provide: OverviewRepository, useClass: OverviewMockRepository },
  ];
}
