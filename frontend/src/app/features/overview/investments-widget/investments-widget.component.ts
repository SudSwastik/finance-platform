import { Component, Input } from '@angular/core';
import { DashboardCardComponent } from '../../../shared/ui/card/dashboard-card.component';
import { Holding } from '../../../shared/models/overview.models';

@Component({
  selector: 'app-investments-widget',
  standalone: true,
  imports: [DashboardCardComponent],
  templateUrl: './investments-widget.component.html',
  styleUrl: './investments-widget.component.scss',
})
export class InvestmentsWidgetComponent {
  @Input({ required: true }) holdings: Holding[] = [];

  protected fmtMoney(val: string): string {
    return `$${parseFloat(val).toLocaleString('en-US', { minimumFractionDigits: 2 })}`;
  }

  protected fmtChange(pct: number): string {
    return `${pct >= 0 ? '+' : ''}${pct}%`;
  }

  protected isPositive(pct: number): boolean {
    return pct >= 0;
  }
}
