import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthShellComponent } from '../auth-shell/auth-shell.component';
import { AuthCardComponent } from '../../../shared/ui/auth-card/auth-card.component';
import { AuthFacade } from '../auth.facade';

@Component({
  selector: 'app-check-email-page',
  standalone: true,
  imports: [AuthShellComponent, AuthCardComponent],
  templateUrl: './check-email-page.component.html',
  styleUrl: './check-email-page.component.scss',
})
export class CheckEmailPageComponent {
  private readonly router = inject(Router);
  private readonly facade = inject(AuthFacade);

  onResend(): void {
    this.facade.resendVerificationEmail();
  }
}
