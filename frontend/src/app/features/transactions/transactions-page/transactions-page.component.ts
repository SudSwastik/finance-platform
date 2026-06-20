import { Component, computed, inject, signal } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { toObservable } from '@angular/core/rxjs-interop';
import { debounceTime, shareReplay, switchMap } from 'rxjs';
import { TransactionRepository } from '../../../data-access/transactions/transaction.repository';
import {
  TransactionItem,
  TransactionTypeGroup,
} from '../../../shared/models/transaction.models';

const PAGE_SIZE = 20;

function currentMonthYM(): string {
  const d = new Date();
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`;
}

@Component({
  selector: 'app-transactions-page',
  standalone: true,
  imports: [AsyncPipe],
  templateUrl: './transactions-page.component.html',
  styleUrl: './transactions-page.component.scss',
})
export class TransactionsPageComponent {
  private readonly repo = inject(TransactionRepository);

  readonly typeGroup = signal<'ALL' | TransactionTypeGroup>('ALL');
  readonly searchTerm = signal('');
  readonly currentMonth = signal<string | undefined>(currentMonthYM());
  readonly page = signal(0);

  private readonly filter = computed(() => ({
    typeGroup: this.typeGroup() === 'ALL' ? undefined : (this.typeGroup() as TransactionTypeGroup),
    search: this.searchTerm() || undefined,
    month: this.currentMonth(),
  }));

  private readonly query = computed(() => ({ filter: this.filter(), page: this.page() }));

  readonly result$ = toObservable(this.query).pipe(
    debounceTime(150),
    switchMap(({ filter, page }) => this.repo.getPage(filter, page, PAGE_SIZE)),
    shareReplay(1),
  );

  readonly stats$ = toObservable(this.currentMonth).pipe(
    switchMap(month => this.repo.getStats(month)),
    shareReplay(1),
  );

  setTypeGroup(group: 'ALL' | TransactionTypeGroup): void {
    this.typeGroup.set(group);
    this.page.set(0);
  }

  onSearch(value: string): void {
    this.searchTerm.set(value);
    this.page.set(0);
  }

  toggleMonth(): void {
    this.currentMonth.update(m => (m ? undefined : currentMonthYM()));
    this.page.set(0);
  }

  goToPage(p: number): void {
    this.page.set(p);
  }

  formatMonth(ym?: string): string {
    if (!ym) return 'All time';
    const [y, m] = ym.split('-').map(Number);
    return new Date(y, m - 1).toLocaleString('en-US', { month: 'short', year: 'numeric' });
  }

  formatDate(dateStr: string): string {
    const [, m, d] = dateStr.split('-').map(Number);
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return `${months[m - 1]} ${d}`;
  }

  amountDisplay(tx: TransactionItem): string {
    const num = parseFloat(tx.amount).toLocaleString('en-IN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
    return `${tx.type === 'CREDIT' ? '+' : '−'}₹${num}`;
  }

  isPositiveType(tx: TransactionItem): boolean {
    return tx.type === 'CREDIT';
  }

  statusLabel(status: string): string {
    const map: Record<string, string> = {
      SETTLED: 'Settled', PENDING: 'Pending', FAILED: 'Failed', REVERSED: 'Reversed',
    };
    return map[status] ?? status;
  }

  iconFor(tx: TransactionItem): { icon: string; bg: string; color: string } {
    if (tx.type === 'CREDIT') return { icon: 'ph-buildings', bg: '#16271F', color: '#4FAE85' };
    const cat = (tx.category ?? '').toLowerCase();
    if (cat.includes('grocer')) return { icon: 'ph-shopping-cart-simple', bg: '#1C1C21', color: '#A8A8AE' };
    if (cat.includes('electron')) return { icon: 'ph-device-mobile', bg: '#1C1C21', color: '#A8A8AE' };
    if (cat.includes('fuel') || cat.includes('petrol')) return { icon: 'ph-gas-pump', bg: '#1C1C21', color: '#A8A8AE' };
    if (cat.includes('food') || cat.includes('dining')) return { icon: 'ph-fork-knife', bg: '#1C1C21', color: '#A8A8AE' };
    if (cat.includes('rent') || cat.includes('housing')) return { icon: 'ph-house-line', bg: '#1C1C21', color: '#A8A8AE' };
    if (tx.type === 'BUY' || tx.type === 'SELL') return { icon: 'ph-chart-bar', bg: '#1C1C21', color: '#A8A8AE' };
    return { icon: 'ph-receipt', bg: '#1C1C21', color: '#A8A8AE' };
  }

  formatStatMoney(amount: string): string {
    const num = parseFloat(amount).toLocaleString('en-IN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
    return `₹${num}`;
  }

  isNetFlowPositive(netFlow: string): boolean {
    return parseFloat(netFlow) >= 0;
  }

  pageNumbers(totalPages: number): (number | '...')[] {
    if (totalPages <= 6) return Array.from({ length: totalPages }, (_, i) => i);
    const cur = this.page();
    const shown = new Set(
      [0, cur - 1, cur, cur + 1, totalPages - 1].filter(p => p >= 0 && p < totalPages),
    );
    const sorted = [...shown].sort((a, b) => a - b);
    const result: (number | '...')[] = [];
    let prev = -1;
    for (const p of sorted) {
      if (p - prev > 1) result.push('...');
      result.push(p);
      prev = p;
    }
    return result;
  }

  showingFrom(page: number): number {
    return page * PAGE_SIZE + 1;
  }

  showingTo(page: number, total: number): number {
    return Math.min((page + 1) * PAGE_SIZE, total);
  }
}
