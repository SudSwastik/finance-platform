import { Component, inject } from '@angular/core';
import { OverviewRepository } from '../../data-access/overview/overview.repository';

@Component({
  selector: 'app-overview-page',
  standalone: true,
  imports: [],
  template: `
    <div class="overview-page">
      <h1 class="text-card-title">Overview</h1>
      <p class="text-secondary">Dashboard widgets — Phase 2</p>
    </div>
  `,
  styles: [`
    .overview-page { padding: 8px 0; }
  `],
})
export class OverviewPageComponent {
  protected readonly overview$ = inject(OverviewRepository).getOverview();
}
