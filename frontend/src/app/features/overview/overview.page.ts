import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { OverviewFacade } from './overview.facade';
import { TotalBudgetsComponent } from './total-budgets/total-budgets.component';
import { SpendingThisMonthComponent } from './spending-this-month/spending-this-month.component';
import { GoalsWidgetComponent } from './goals-widget/goals-widget.component';
import { TransactionsWidgetComponent } from './transactions-widget/transactions-widget.component';
import { InvestmentsWidgetComponent } from './investments-widget/investments-widget.component';
import { RecurringWidgetComponent } from './recurring-widget/recurring-widget.component';

@Component({
  selector: 'app-overview-page',
  standalone: true,
  providers: [OverviewFacade],
  imports: [
    AsyncPipe,
    TotalBudgetsComponent,
    SpendingThisMonthComponent,
    GoalsWidgetComponent,
    TransactionsWidgetComponent,
    InvestmentsWidgetComponent,
    RecurringWidgetComponent,
  ],
  templateUrl: './overview.page.html',
  styleUrl: './overview.page.scss',
})
export class OverviewPageComponent {
  protected readonly facade = inject(OverviewFacade);
}
