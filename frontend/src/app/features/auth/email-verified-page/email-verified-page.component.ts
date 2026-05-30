import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthShellComponent } from '../auth-shell/auth-shell.component';
import { AuthCardComponent } from '../../../shared/ui/auth-card/auth-card.component';

@Component({
  selector: 'app-email-verified-page',
  standalone: true,
  imports: [AuthShellComponent, AuthCardComponent],
  templateUrl: './email-verified-page.component.html',
  styleUrl: './email-verified-page.component.scss',
})
export class EmailVerifiedPageComponent {
  private readonly router = inject(Router);

  onContinue(): void {
    this.router.navigate(['/register/onboarding/account-type']);
  }
}
