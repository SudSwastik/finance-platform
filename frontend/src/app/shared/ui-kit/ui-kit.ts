import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';

export interface ModalConfig {
  title: string;
  confirmLabel?: string;
  cancelLabel?: string;
  data?: unknown;
}

export interface ModalResult<T = unknown> {
  confirmed: boolean;
  data?: T;
}

export abstract class UiKitModalAdapter {
  abstract open(config: ModalConfig): Observable<ModalResult>;
}

export const UI_KIT_MODAL_ADAPTER = new InjectionToken<UiKitModalAdapter>(
  'UiKitModalAdapter'
);
