import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { ModalConfig, ModalState } from './modal.model';

@Injectable({ providedIn: 'root' })
export class ModalService {
  private readonly _state$ = new BehaviorSubject<ModalState>({ open: false, config: null });
  private _result$ = new Subject<boolean>();

  readonly state$ = this._state$.asObservable();

  open(config: ModalConfig): Observable<boolean> {
    this._result$ = new Subject<boolean>();
    this._state$.next({ open: true, config });
    return this._result$.asObservable();
  }

  confirm(): void {
    this._close(true);
  }

  cancel(): void {
    this._close(false);
  }

  private _close(confirmed: boolean): void {
    this._state$.next({ open: false, config: null });
    this._result$.next(confirmed);
    this._result$.complete();
  }
}
