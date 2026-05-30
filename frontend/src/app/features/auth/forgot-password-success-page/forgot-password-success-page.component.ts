import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthShellComponent } from '../auth-shell/auth-shell.component';
import { AuthCardComponent } from '../../../shared/ui/auth-card/auth-card.component';

@Component({
  selector: 'app-forgot-password-success-page',
  standalone: true,
  imports: [AuthShellComponent, AuthCardComponent],
  templateUrl: './forgot-password-success-page.component.html',
  styleUrl: './forgot-password-success-page.component.scss',
})
export class ForgotPasswordSuccessPageComponent {
  private readonly router = inject(Router);

  onContinue(): void {
    this.router.navigate(['/login']);
  }
}
