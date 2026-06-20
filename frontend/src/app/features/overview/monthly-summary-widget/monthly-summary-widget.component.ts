import { Component, Input } from '@angular/core';
import { DashboardCardComponent } from '../../../shared/ui/card/dashboard-card.component';
import { MonthlySummary } from '../../../shared/models/overview.models';

@Component({
  selector: 'app-monthly-summary-widget',
  standalone: true,
  imports: [DashboardCardComponent],
  templateUrl: './monthly-summary-widget.component.html',
  styleUrl: './monthly-summary-widget.component.scss',
})
export class MonthlySummaryWidgetComponent {
  @Input({ required: true }) summary!: MonthlySummary;

  protected fmt(val: string): string {
    return `$${parseFloat(val).toLocaleString('en-US', { minimumFractionDigits: 2 })}`;
  }

  protected spendPct(): string {
    const income = parseFloat(this.summary.income);
    const spend  = parseFloat(this.summary.spending);
    if (!income) return '0%';
    return `${Math.min(100, Math.round((spend / income) * 100))}%`;
  }
}
