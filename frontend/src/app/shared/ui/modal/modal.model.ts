import { TemplateRef } from '@angular/core';

export interface ModalConfig {
  title: string;
  confirmLabel?: string;
  cancelLabel?: string;
  bodyTemplate?: TemplateRef<unknown>;
}

export interface ModalState {
  open: boolean;
  config: ModalConfig | null;
}
