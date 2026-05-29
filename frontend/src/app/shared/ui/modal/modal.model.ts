export interface ModalConfig {
  title: string;
  confirmLabel?: string;
  cancelLabel?: string;
}

export interface ModalState {
  open: boolean;
  config: ModalConfig | null;
}
