import { Component, inject } from '@angular/core';
import { AsyncPipe, NgTemplateOutlet } from '@angular/common';
import { ModalService } from './modal.service';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [AsyncPipe, NgTemplateOutlet],
  templateUrl: './app-modal.component.html',
  styleUrl: './app-modal.component.scss',
})
export class AppModalComponent {
  protected readonly modalService = inject(ModalService);
  protected readonly state$ = this.modalService.state$;
}
