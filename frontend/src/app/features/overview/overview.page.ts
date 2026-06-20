import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { OverviewFacade } from './overview.facade';
import { TotalBudgetsComponent } from './total-budgets/total-budgets.component';
import { InvestmentsWidgetComponent } from './investments-widget/investments-widget.component';
import { RecurringWidgetComponent } from './recurring-widget/recurring-widget.component';
import { RecentTransactionsWidgetComponent } from './recent-transactions-widget/recent-transactions-widget.component';
import { MonthlySummaryWidgetComponent } from './monthly-summary-widget/monthly-summary-widget.component';

@Component({
  selector: 'app-overview-page',
  standalone: true,
  providers: [OverviewFacade],
  imports: [
    AsyncPipe,
    TotalBudgetsComponent,
    InvestmentsWidgetComponent,
    RecurringWidgetComponent,
    RecentTransactionsWidgetComponent,
    MonthlySummaryWidgetComponent,
  ],
  templateUrl: './overview.page.html',
  styleUrl: './overview.page.scss',
})
export class OverviewPageComponent {
  protected readonly facade = inject(OverviewFacade);
}
