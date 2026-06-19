import { Provider } from '@angular/core';
import { environment } from '../../environments/environment';
import { OverviewRepository } from './overview/overview.repository';
import { OverviewMockRepository } from './overview/overview-mock.repository';
import { OverviewHttpRepository } from './overview/overview-http.repository';

export function provideDataAccess(): Provider[] {
  if (environment.useMockData) {
    return [{ provide: OverviewRepository, useClass: OverviewMockRepository }];
  }
  return [{ provide: OverviewRepository, useClass: OverviewHttpRepository }];
}
