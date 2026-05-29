import { Component, Input } from '@angular/core';
import { DashboardCardComponent } from '../../../shared/ui/card/dashboard-card.component';
import { TotalBudgetsSection, BudgetCategory } from '../../../shared/models/overview.models';

@Component({
  selector: 'app-total-budgets',
  standalone: true,
  imports: [DashboardCardComponent],
  templateUrl: './total-budgets.component.html',
  styleUrl: './total-budgets.component.scss',
})
export class TotalBudgetsComponent {
  @Input({ required: true }) data!: TotalBudgetsSection;

  protected segmentWidth(cat: BudgetCategory): string {
    const total = this.data.categories.reduce((s, c) => s + parseFloat(c.budget), 0);
    return `${(parseFloat(cat.budget) / total) * 100}%`;
  }

  protected categoryColor(token: string): string {
    return `var(--color-category-${token})`;
  }

  protected fmt(val: string): string {
    return `$${parseFloat(val).toLocaleString('en-US')}`;
  }
}
