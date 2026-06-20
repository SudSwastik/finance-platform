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

  protected isCredit(t: RecentTransaction): boolean { return t.type === 'CREDIT'; }
  protected isDebit(t: RecentTransaction): boolean  { return t.type === 'DEBIT' || t.type === 'FEE'; }

  protected iconClass(t: RecentTransaction): string {
    const map: Record<string, string> = {
      CREDIT:   'ph-buildings',
      DEBIT:    'ph-shopping-cart-simple',
      BUY:      'ph-currency-btc',
      SELL:     'ph-trend-down',
      TRANSFER: 'ph-arrows-left-right',
      FEE:      'ph-receipt',
    };
    return map[t.type] ?? 'ph-circle';
  }
}
