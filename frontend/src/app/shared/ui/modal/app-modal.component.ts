import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { ModalService } from './modal.service';
import { AppButtonComponent } from '../button/app-button.component';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [AsyncPipe, AppButtonComponent],
  templateUrl: './app-modal.component.html',
  styleUrl: './app-modal.component.scss',
})
export class AppModalComponent {
  protected readonly modalService = inject(ModalService);
  protected readonly state$ = this.modalService.state$;
}
