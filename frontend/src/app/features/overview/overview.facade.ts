import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { catchError, finalize, map, of, shareReplay, tap } from 'rxjs';
import { OverviewData } from '../../shared/models/overview.models';
import { OverviewRepository } from '../../data-access/overview/overview.repository';
import { GoalItem } from '../../shared/models/goal.models';
import { GoalRepository } from '../../data-access/goals/goal.repository';

@Injectable()
export class OverviewFacade {
  private readonly _loading$ = new BehaviorSubject<boolean>(true);
  private readonly _error$ = new BehaviorSubject<string | null>(null);

  readonly loading$: Observable<boolean> = this._loading$.asObservable();
  readonly error$: Observable<string | null> = this._error$.asObservable();

  readonly overview$: Observable<OverviewData | null> = inject(OverviewRepository)
    .getOverview()
    .pipe(
      tap(() => this._error$.next(null)),
      catchError(() => {
        this._error$.next('Failed to load overview. Is the backend running?');
        return of(null);
      }),
      finalize(() => this._loading$.next(false)),
      shareReplay(1),
    );

  // Fetched directly from goals-service (not composed by the BFF yet) so a
  // goals-service outage only empties this widget, not the whole page.
  readonly goals$: Observable<GoalItem[]> = inject(GoalRepository)
    .list()
    .pipe(
      map(goals => goals.slice(0, 3)),
      catchError(() => of([])),
      shareReplay(1),
    );
}
