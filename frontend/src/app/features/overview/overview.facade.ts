import { Injectable, inject } from '@angular/core';
import { shareReplay } from 'rxjs/operators';
import { OverviewRepository } from '../../data-access/overview/overview.repository';

@Injectable()
export class OverviewFacade {
  readonly overview$ = inject(OverviewRepository).getOverview().pipe(shareReplay(1));
}
