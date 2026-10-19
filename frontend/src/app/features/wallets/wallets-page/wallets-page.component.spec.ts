import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
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
        provideRouter([]),
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

  it('explains an empty account list without rendering blank selection panels', () => {
    const fixture = TestBed.createComponent(WalletsPageComponent);
    fixture.detectChanges();
    const page = fixture.nativeElement as HTMLElement;
    expect(page.querySelector('.wallets-page__empty-state')?.textContent).toContain('No accounts yet');
    expect(page.querySelector('.wallets-page__list-card')).toBeNull();
    expect(page.textContent).not.toContain('Select an account');
    expect(page.querySelector<HTMLButtonElement>('.wallets-page__link-btn')?.disabled).toBeTrue();
  });

  it('keeps account selection and details available when accounts exist', () => {
    spyOn(TestBed.inject(AccountRepository), 'list').and.returnValue(of([
      { id: 'acc-1', type: 'BANK', name: 'Test Bank', currency: 'INR', balance: '0.00' },
    ]));
    const fixture = TestBed.createComponent(WalletsPageComponent);
    fixture.detectChanges();
    TestBed.flushEffects();
    fixture.detectChanges();
    const page = fixture.nativeElement as HTMLElement;
    expect(page.querySelector('.wallets-page__empty-state')).toBeNull();
    expect(page.querySelector('.wallets-page__account-row')?.textContent).toContain('Test Bank');
    expect(page.querySelector('.wallets-page__balance-card')?.textContent).toContain('Test Bank');
  });
});
