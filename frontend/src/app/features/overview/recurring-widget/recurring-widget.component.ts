import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DashboardCardComponent } from '../../../shared/ui/card/dashboard-card.component';
import { RecurringBill } from '../../../shared/models/overview.models';

@Component({
  selector: 'app-recurring-widget',
  standalone: true,
  imports: [DashboardCardComponent, RouterLink],
  templateUrl: './recurring-widget.component.html',
  styleUrl: './recurring-widget.component.scss',
})
export class RecurringWidgetComponent {
  @Input({ required: true }) bills: RecurringBill[] = [];

  protected fmtAmount(val: string): string {
    return `$${parseFloat(val).toFixed(2)}`;
  }

  protected fmtDate(iso: string): string {
    const d = new Date(iso + 'T00:00:00');
    return `${d.getDate()} ${d.toLocaleString('en-US', { month: 'short' })}`;
  }

  protected fmtFrequency(f: string): string {
    return f.charAt(0).toUpperCase() + f.slice(1);
  }
}
