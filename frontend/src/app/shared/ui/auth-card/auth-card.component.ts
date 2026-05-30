import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-auth-card',
  standalone: true,
  templateUrl: './auth-card.component.html',
  styleUrl: './auth-card.component.scss',
})
export class AuthCardComponent {
  @Input({ required: true }) title!: string;
  @Input() subtitle?: string;
  @Input() primaryLabel?: string;
  @Input() iconVariant: 'default' | 'green' | 'green-filled' | 'dark' = 'default';
  @Input() iconBadge: 'check' | null = null;
  @Output() primaryClick = new EventEmitter<void>();
}
