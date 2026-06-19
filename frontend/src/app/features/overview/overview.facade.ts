import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { catchError, finalize, of, shareReplay, tap } from 'rxjs';
import { OverviewData } from '../../shared/models/overview.models';
import { OverviewRepository } from '../../data-access/overview/overview.repository';

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
}
