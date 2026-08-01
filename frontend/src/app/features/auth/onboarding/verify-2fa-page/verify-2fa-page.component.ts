import { Component, ElementRef, inject, QueryList, ViewChildren } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthCardComponent } from '../../../../shared/ui/auth-card/auth-card.component';
import { AuthFacade } from '../../auth.facade';

@Component({
  selector: 'app-verify-2fa-page',
  standalone: true,
  imports: [FormsModule, AuthCardComponent],
  templateUrl: './verify-2fa-page.component.html',
  styleUrl: './verify-2fa-page.component.scss',
})
export class Verify2faPageComponent {
  private readonly router = inject(Router);
  private readonly facade = inject(AuthFacade);

  @ViewChildren('digitInput') digitInputs!: QueryList<ElementRef<HTMLInputElement>>;

  // Real phone/SMS verification is deferred — this step is currently a UI placeholder.
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

  async onContinue(): Promise<void> {
    this.errorMessage = '';
    this.submitting = true;
    try {
      await this.facade.finishOnboarding();
      this.router.navigate(['/overview']);
    } catch (err) {
      this.errorMessage = err instanceof Error ? err.message : 'Could not finish setting up your account.';
    } finally {
      this.submitting = false;
    }
  }
}
