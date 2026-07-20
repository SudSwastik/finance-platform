import { Component, computed, inject, signal } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { toObservable } from '@angular/core/rxjs-interop';
import { debounceTime, shareReplay, switchMap } from 'rxjs';
import { SubscriptionRepository } from '../../../data-access/subscriptions/subscription.repository';
import {
  RecurringFrequency,
  SubscriptionItem,
} from '../../../shared/models/subscription.models';

const PAGE_SIZE = 20;

@Component({
  selector: 'app-subscriptions-page',
  standalone: true,
  imports: [AsyncPipe],
  templateUrl: './subscriptions-page.component.html',
  styleUrl: './subscriptions-page.component.scss',
})
export class SubscriptionsPageComponent {
  private readonly repo = inject(SubscriptionRepository);

  readonly frequency = signal<'ALL' | RecurringFrequency>('ALL');
  readonly searchTerm = signal('');
  readonly page = signal(0);

  private readonly filter = computed(() => ({
    frequency: this.frequency() === 'ALL' ? undefined : (this.frequency() as RecurringFrequency),
    search: this.searchTerm() || undefined,
  }));

  private readonly query = computed(() => ({ filter: this.filter(), page: this.page() }));

  readonly result$ = toObservable(this.query).pipe(
    debounceTime(150),
    switchMap(({ filter, page }) => this.repo.getPage(filter, page, PAGE_SIZE)),
    shareReplay(1),
  );

  readonly stats$ = this.repo.getStats().pipe(shareReplay(1));

  setFrequency(freq: 'ALL' | RecurringFrequency): void {
    this.frequency.set(freq);
    this.page.set(0);
  }

  onSearch(value: string): void {
    this.searchTerm.set(value);
    this.page.set(0);
  }

  goToPage(p: number): void {
    this.page.set(p);
  }

  formatDate(dateStr: string | null): string {
    if (!dateStr) return '—';
    const [, m, d] = dateStr.split('-').map(Number);
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return `${months[m - 1]} ${d}`;
  }

  amountDisplay(sub: SubscriptionItem): string {
    const num = parseFloat(sub.amount).toLocaleString('en-IN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
    return `−₹${num}`;
  }

  formatStatMoney(amount: string): string {
    const num = parseFloat(amount).toLocaleString('en-IN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
    return `₹${num}`;
  }

  frequencyLabel(freq: RecurringFrequency | null): string {
    if (!freq) return '—';
    const map: Record<RecurringFrequency, string> = {
      WEEKLY: 'Weekly', MONTHLY: 'Monthly', YEARLY: 'Yearly',
    };
    return map[freq];
  }

  statusLabel(status: string): string {
    const map: Record<string, string> = {
      SETTLED: 'Settled', PENDING: 'Pending', FAILED: 'Failed', REVERSED: 'Reversed',
    };
    return map[status] ?? status;
  }

  iconFor(sub: SubscriptionItem): { icon: string; bg: string; color: string } {
    const cat = (sub.category ?? '').toLowerCase();
    if (cat.includes('entertain')) return { icon: 'ph-play-circle', bg: '#1C1C21', color: '#A8A8AE' };
    if (cat.includes('software') || cat.includes('tech')) return { icon: 'ph-code', bg: '#1C1C21', color: '#A8A8AE' };
    if (cat.includes('fitness') || cat.includes('health')) return { icon: 'ph-heartbeat', bg: '#1C1C21', color: '#A8A8AE' };
    if (cat.includes('news') || cat.includes('media')) return { icon: 'ph-newspaper', bg: '#1C1C21', color: '#A8A8AE' };
    return { icon: 'ph-arrows-clockwise', bg: '#1C1C21', color: '#A8A8AE' };
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
