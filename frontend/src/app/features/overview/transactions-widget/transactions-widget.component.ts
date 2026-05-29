import { Component, Input } from '@angular/core';
import { DashboardCardComponent } from '../../../shared/ui/card/dashboard-card.component';
import { Transaction } from '../../../shared/models/overview.models';

const CATEGORY_LABEL: Record<string, string> = {
  essential: 'Essential',
  lifestyle: 'Lifestyle',
  occasional: 'Occasional',
  other: 'Other',
};

const CATEGORY_TOKEN: Record<string, string> = {
  essential: 'essentials',
  lifestyle: 'lifestyles',
  occasional: 'occasional',
  other: 'others',
};

@Component({
  selector: 'app-transactions-widget',
  standalone: true,
  imports: [DashboardCardComponent],
  templateUrl: './transactions-widget.component.html',
  styleUrl: './transactions-widget.component.scss',
})
export class TransactionsWidgetComponent {
  @Input({ required: true }) transactions: Transaction[] = [];

  protected pillLabel(cat: string): string {
    return CATEGORY_LABEL[cat] ?? cat;
  }

  protected pillToken(cat: string): string {
    return CATEGORY_TOKEN[cat] ?? cat;
  }

  protected fmtAmount(val: string): string {
    const n = parseFloat(val);
    return n < 0 ? `-$${Math.abs(n).toFixed(2)}` : `+$${n.toFixed(2)}`;
  }

  protected fmtDate(iso: string): string {
    const d = new Date(iso + 'T00:00:00');
    return `${d.getDate()} ${d.toLocaleString('en-US', { month: 'short' })}`;
  }
}
