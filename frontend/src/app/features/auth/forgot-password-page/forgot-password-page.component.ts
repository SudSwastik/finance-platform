import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthShellComponent } from '../auth-shell/auth-shell.component';
import { AuthCardComponent } from '../../../shared/ui/auth-card/auth-card.component';
import { AuthFacade } from '../auth.facade';

@Component({
  selector: 'app-forgot-password-page',
  standalone: true,
  imports: [FormsModule, AuthShellComponent, AuthCardComponent],
  templateUrl: './forgot-password-page.component.html',
  styleUrl: './forgot-password-page.component.scss',
})
export class ForgotPasswordPageComponent {
  private readonly router = inject(Router);
  private readonly facade = inject(AuthFacade);

  email = '';
  submitting = false;
  errorMessage = '';

  async onSend(): Promise<void> {
    if (this.submitting) return;
    this.errorMessage = '';
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email.trim())) {
      this.errorMessage = 'Enter a valid email address.';
      return;
    }
    this.submitting = true;
    try {
      await this.facade.forgotPassword(this.email.trim());
      await this.router.navigate(['/forgot-password/success']);
    } catch (err) {
      this.errorMessage = err instanceof Error ? err.message : 'Could not send a reset code. Please try again.';
    } finally {
      this.submitting = false;
    }
  }
}
