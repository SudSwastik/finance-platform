import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { WalletsPageComponent } from './wallets-page.component';
import { AccountRepository } from '../../../data-access/accounts/account.repository';
import { TransactionRepository } from '../../../data-access/transactions/transaction.repository';
import { AccountDetail, AccountItem } from '../../../shared/models/account.models';
import { TransactionPage, TransactionStats } from '../../../shared/models/transaction.models';

class FakeAccountRepository extends AccountRepository {
  override list() {
    return of<AccountItem[]>([]);
  }

  override getById() {
    return of<AccountDetail>({
      id: 'acc-1', type: 'BANK', name: 'Test Bank', currency: 'INR', balance: '0.00',
      monthChange: '0.00', moneyInMonth: '0.00', moneyOutMonth: '0.00', avgDailyMonth: '0.00',
    });
  }
}

class FakeTransactionRepository extends TransactionRepository {
  override getPage() {
    return of<TransactionPage>({ content: [], totalElements: 0, page: 0, size: 5, totalPages: 0 });
  }

  override getStats() {
    return of<TransactionStats>({
      moneyIn: '0.00', moneyInCount: 0, moneyOut: '0.00', moneyOutCount: 0, netFlow: '0.00', totalCount: 0,
    });
  }
}

describe('WalletsPageComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WalletsPageComponent],
      providers: [
        { provide: AccountRepository, useClass: FakeAccountRepository },
        { provide: TransactionRepository, useClass: FakeTransactionRepository },
      ],
    }).compileComponents();
  });

  it('renders the .wallets-page root class', () => {
    const fixture = TestBed.createComponent(WalletsPageComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.wallets-page')).toBeTruthy();
  });
});
