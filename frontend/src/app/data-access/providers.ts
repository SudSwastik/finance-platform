import { Provider } from '@angular/core';
import { environment } from '../../environments/environment';
import { OverviewRepository } from './overview/overview.repository';
import { OverviewMockRepository } from './overview/overview-mock.repository';
import { OverviewHttpRepository } from './overview/overview-http.repository';
import { TransactionRepository } from './transactions/transaction.repository';
import { TransactionHttpRepository } from './transactions/transaction-http.repository';
import { SubscriptionRepository } from './subscriptions/subscription.repository';
import { SubscriptionHttpRepository } from './subscriptions/subscription-http.repository';
import { AccountRepository } from './accounts/account.repository';
import { AccountHttpRepository } from './accounts/account-http.repository';
import { PortfolioRepository } from './portfolio/portfolio.repository';
import { PortfolioHttpRepository } from './portfolio/portfolio-http.repository';

export function provideDataAccess(): Provider[] {
  const overviewProvider = environment.useMockData
    ? { provide: OverviewRepository, useClass: OverviewMockRepository }
    : { provide: OverviewRepository, useClass: OverviewHttpRepository };

  return [
    overviewProvider,
    { provide: TransactionRepository, useClass: TransactionHttpRepository },
    { provide: SubscriptionRepository, useClass: SubscriptionHttpRepository },
    { provide: AccountRepository, useClass: AccountHttpRepository },
    { provide: PortfolioRepository, useClass: PortfolioHttpRepository },
  ];
}
