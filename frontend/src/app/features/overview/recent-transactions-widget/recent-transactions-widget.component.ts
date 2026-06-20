import { Component, Input } from '@angular/core';
import { DashboardCardComponent } from '../../../shared/ui/card/dashboard-card.component';
import { RecentTransaction } from '../../../shared/models/overview.models';

@Component({
  selector: 'app-recent-transactions-widget',
  standalone: true,
  imports: [DashboardCardComponent],
  templateUrl: './recent-transactions-widget.component.html',
  styleUrl: './recent-transactions-widget.component.scss',
})
export class RecentTransactionsWidgetComponent {
  @Input({ required: true }) transactions: RecentTransaction[] = [];

  protected fmt(val: string): string {
    return parseFloat(val).toLocaleString('en-US', { minimumFractionDigits: 2 });
  }

  protected fmtDate(date: string): string {
    return new Date(date).toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
  }

  protected isCredit(t: RecentTransaction): boolean {
    return t.type === 'CREDIT';
  }

  protected isDebit(t: RecentTransaction): boolean {
    return t.type === 'DEBIT' || t.type === 'FEE';
  }

  protected typeLabel(t: RecentTransaction): string {
    return t.type.charAt(0) + t.type.slice(1).toLowerCase();
  }

  protected initials(name: string): string {
    return name.split(' ').map(w => w[0]).slice(0, 2).join('').toUpperCase();
  }
}
