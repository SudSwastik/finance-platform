import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { OverviewFacade } from './overview.facade';
import { TotalBudgetsComponent } from './total-budgets/total-budgets.component';
import { InvestmentsWidgetComponent } from './investments-widget/investments-widget.component';
import { RecurringWidgetComponent } from './recurring-widget/recurring-widget.component';

@Component({
  selector: 'app-overview-page',
  standalone: true,
  providers: [OverviewFacade],
  imports: [
    AsyncPipe,
    TotalBudgetsComponent,
    InvestmentsWidgetComponent,
    RecurringWidgetComponent,
  ],
  templateUrl: './overview.page.html',
  styleUrl: './overview.page.scss',
})
export class OverviewPageComponent {
  protected readonly facade = inject(OverviewFacade);
}
