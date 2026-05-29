import { Component, Input } from '@angular/core';
import { DashboardCardComponent } from '../../../shared/ui/card/dashboard-card.component';
import { Goal } from '../../../shared/models/overview.models';

@Component({
  selector: 'app-goals-widget',
  standalone: true,
  imports: [DashboardCardComponent],
  templateUrl: './goals-widget.component.html',
  styleUrl: './goals-widget.component.scss',
})
export class GoalsWidgetComponent {
  @Input({ required: true }) goals: Goal[] = [];

  protected goalColor(token: string): string {
    return `var(--color-${token.replace(/\./g, '-')})`;
  }

  protected fmt(val: string): string {
    return `$${parseFloat(val).toLocaleString('en-US')}`;
  }

  protected fmtDate(iso: string): string {
    const d = new Date(iso + 'T00:00:00');
    return d.toLocaleString('en-US', { month: 'short', year: 'numeric' });
  }
}
