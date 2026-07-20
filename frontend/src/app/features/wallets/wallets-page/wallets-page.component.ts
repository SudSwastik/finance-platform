import { Component, inject, signal } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { toObservable } from '@angular/core/rxjs-interop';
import { map, of, shareReplay, switchMap, tap } from 'rxjs';
import { AccountRepository } from '../../../data-access/accounts/account.repository';
import { TransactionRepository } from '../../../data-access/transactions/transaction.repository';
import { AccountItem, AccountType } from '../../../shared/models/account.models';
import { TransactionItem } from '../../../shared/models/transaction.models';

type GroupKey = 'BANK' | 'CREDIT_CARD' | 'EXCHANGES';

interface AccountGroup {
  key: GroupKey;
  label: string;
  accounts: AccountItem[];
}

const GROUP_ORDER: GroupKey[] = ['BANK', 'CREDIT_CARD', 'EXCHANGES'];
const GROUP_LABELS: Record<GroupKey, string> = {
  BANK: 'Banks',
  CREDIT_CARD: 'Cards',
  EXCHANGES: 'Exchanges & Cash',
};

function groupKey(type: AccountType): GroupKey {
  if (type === 'BANK') return 'BANK';
  if (type === 'CREDIT_CARD') return 'CREDIT_CARD';
  return 'EXCHANGES';
}

@Component({
  selector: 'app-wallets-page',
  standalone: true,
  imports: [AsyncPipe, RouterLink],
  templateUrl: './wallets-page.component.html',
  styleUrl: './wallets-page.component.scss',
})
export class WalletsPageComponent {
  private readonly accountRepo = inject(AccountRepository);
  private readonly txnRepo = inject(TransactionRepository);

  readonly selectedAccountId = signal<string | null>(null);

  readonly accounts$ = this.accountRepo.list().pipe(
    tap(accounts => {
      if (!this.selectedAccountId() && accounts.length) {
        const firstBank = accounts.find(a => a.type === 'BANK');
        this.selectedAccountId.set((firstBank ?? accounts[0]).id);
      }
    }),
    shareReplay(1),
  );

  readonly groupedAccounts$ = this.accounts$.pipe(map(accounts => this.group(accounts)));

  readonly totalCash$ = this.accounts$.pipe(map(accounts => this.sumBalance(accounts, 'BANK')));
  readonly creditCardBalance$ = this.accounts$.pipe(map(accounts => this.sumBalance(accounts, 'CREDIT_CARD')));

  readonly detail$ = toObservable(this.selectedAccountId).pipe(
    switchMap(id => (id ? this.accountRepo.getById(id) : of(null))),
    shareReplay(1),
  );

  readonly activity$ = toObservable(this.selectedAccountId).pipe(
    switchMap(id => (id ? this.txnRepo.getPage({ accountId: id }, 0, 5) : of(null))),
    shareReplay(1),
  );

  selectAccount(id: string): void {
    this.selectedAccountId.set(id);
  }

  private group(accounts: AccountItem[]): AccountGroup[] {
    return GROUP_ORDER
      .map(key => ({
        key,
        label: GROUP_LABELS[key],
        accounts: accounts.filter(a => groupKey(a.type) === key),
      }))
      .filter(g => g.accounts.length > 0);
  }

  private sumBalance(accounts: AccountItem[], type: AccountType): number {
    return accounts
      .filter(a => a.type === type)
      .reduce((sum, a) => sum + parseFloat(a.balance), 0);
  }

  iconFor(type: AccountType): { icon: string; bg: string; color: string } {
    switch (type) {
      case 'BANK':          return { icon: 'ph-bank', bg: '#1C1C21', color: '#A8A8AE' };
      case 'CREDIT_CARD':   return { icon: 'ph-credit-card', bg: '#1C1C21', color: '#A8A8AE' };
      case 'BROKERAGE':     return { icon: 'ph-chart-pie-slice', bg: '#1C1C21', color: '#A8A8AE' };
      case 'CRYPTO_WALLET': return { icon: 'ph-currency-btc', bg: '#1C1C21', color: '#A8A8AE' };
    }
  }

  txIconFor(tx: TransactionItem): { icon: string; bg: string; color: string } {
    if (tx.type === 'CREDIT') return { icon: 'ph-buildings', bg: '#16271F', color: '#4FAE85' };
    if (tx.type === 'TRANSFER') return { icon: 'ph-arrows-left-right', bg: '#1C1C21', color: '#A8A8AE' };
    const cat = (tx.category ?? '').toLowerCase();
    if (cat.includes('rent') || cat.includes('housing')) return { icon: 'ph-house-line', bg: '#1C1C21', color: '#A8A8AE' };
    if (cat.includes('fuel') || cat.includes('petrol'))  return { icon: 'ph-gas-pump', bg: '#1C1C21', color: '#A8A8AE' };
    if (tx.type === 'BUY' || tx.type === 'SELL')         return { icon: 'ph-chart-bar', bg: '#1C1C21', color: '#A8A8AE' };
    return { icon: 'ph-receipt', bg: '#1C1C21', color: '#A8A8AE' };
  }

  formatAmount(value: number, forceSign = false): string {
    const abs = Math.abs(value).toLocaleString('en-IN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
    if (value < 0) return `−₹${abs}`;
    return forceSign ? `+₹${abs}` : `₹${abs}`;
  }

  balanceDisplay(balance: string): string {
    return this.formatAmount(parseFloat(balance));
  }

  moneyInDisplay(moneyInMonth: string): string {
    return this.formatAmount(parseFloat(moneyInMonth), true);
  }

  moneyOutDisplay(moneyOutMonth: string): string {
    return this.formatAmount(-parseFloat(moneyOutMonth));
  }

  avgDailyDisplay(avgDailyMonth: string): string {
    return this.formatAmount(parseFloat(avgDailyMonth));
  }

  monthChangeText(monthChange: string): string {
    return `${this.formatAmount(parseFloat(monthChange), true)} this month`;
  }

  isPositive(value: string | number): boolean {
    return (typeof value === 'string' ? parseFloat(value) : value) >= 0;
  }

  txAmountDisplay(tx: TransactionItem): string {
    const signed = tx.type === 'CREDIT' || tx.type === 'SELL' ? parseFloat(tx.amount) : -parseFloat(tx.amount);
    return this.formatAmount(signed, true);
  }

  formatDate(dateStr: string): string {
    const [, m, d] = dateStr.split('-').map(Number);
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return `${months[m - 1]} ${d}`;
  }
}
