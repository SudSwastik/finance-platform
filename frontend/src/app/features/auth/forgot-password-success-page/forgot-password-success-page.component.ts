import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthShellComponent } from '../auth-shell/auth-shell.component';
import { AuthCardComponent } from '../../../shared/ui/auth-card/auth-card.component';
import { AuthFacade } from '../auth.facade';

@Component({
  selector: 'app-forgot-password-success-page',
  standalone: true,
  imports: [FormsModule, RouterLink, AuthShellComponent, AuthCardComponent],
  templateUrl: './forgot-password-success-page.component.html',
  styleUrl: './forgot-password-success-page.component.scss',
})
export class ForgotPasswordSuccessPageComponent {
  private readonly router = inject(Router);
  private readonly facade = inject(AuthFacade);

  email = this.facade.passwordResetEmail;
  code = '';
  password = '';
  confirmPassword = '';
  submitting = false;
  completed = false;
  errorMessage = '';

  async onContinue(): Promise<void> {
    if (this.submitting) return;
    if (this.completed) {
      await this.router.navigate(['/login']);
      return;
    }
    this.errorMessage = '';
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email.trim()) || !this.code.trim() || !this.password) {
      this.errorMessage = 'Enter your email, reset code, and new password.';
      return;
    }
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }
    this.submitting = true;
    try {
      await this.facade.confirmForgotPassword(this.email, this.code, this.password);
      this.password = '';
      this.confirmPassword = '';
      this.code = '';
      this.completed = true;
    } catch (err) {
      this.errorMessage = err instanceof Error ? err.message : 'Could not reset your password. Please try again.';
    } finally {
      this.submitting = false;
    }
  }
}
