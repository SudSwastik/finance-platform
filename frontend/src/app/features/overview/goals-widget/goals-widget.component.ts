import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DashboardCardComponent } from '../../../shared/ui/card/dashboard-card.component';
import { GoalItem } from '../../../shared/models/goal.models';

@Component({
  selector: 'app-goals-widget',
  standalone: true,
  imports: [DashboardCardComponent, RouterLink],
  templateUrl: './goals-widget.component.html',
  styleUrl: './goals-widget.component.scss',
})
export class GoalsWidgetComponent {
  @Input({ required: true }) goals: GoalItem[] = [];

  protected clampedPercent(percent: number): number {
    return Math.min(Math.max(percent, 0), 100);
  }

  protected fmtAmount(val: string): string {
    return `$${parseFloat(val).toLocaleString('en-US')}`;
  }
}
