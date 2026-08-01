import { Component, ElementRef, QueryList, ViewChildren, inject } from '@angular/core';
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

  @ViewChildren('digitInput') digitInputs!: QueryList<ElementRef<HTMLInputElement>>;

  digits = ['', '', '', '', '', ''];
  submitting = false;
  errorMessage = '';

  onDigitInput(index: number, event: Event): void {
    const input = event.target as HTMLInputElement;
    const val = input.value.replace(/\D/g, '').slice(-1);
    this.digits[index] = val;
    input.value = val;
    if (val && index < 5) {
      this.digitInputs.toArray()[index + 1].nativeElement.focus();
    }
  }

  onDigitKeydown(index: number, event: KeyboardEvent): void {
    if (event.key === 'Backspace' && !this.digits[index] && index > 0) {
      this.digitInputs.toArray()[index - 1].nativeElement.focus();
    }
  }

  async onResend(): Promise<void> {
    this.errorMessage = '';
    try {
      await this.facade.resendVerificationEmail();
    } catch (err) {
      this.errorMessage = err instanceof Error ? err.message : 'Could not resend the code.';
    }
  }

  async onVerify(): Promise<void> {
    this.errorMessage = '';
    const code = this.digits.join('');
    if (code.length !== 6) {
      this.errorMessage = 'Enter all 6 digits.';
      return;
    }
    this.submitting = true;
    try {
      await this.facade.confirmSignUp(code);
      this.router.navigate(['/register/onboarding']);
    } catch (err) {
      this.errorMessage = err instanceof Error ? err.message : 'That code is invalid or expired.';
    } finally {
      this.submitting = false;
    }
  }
}
