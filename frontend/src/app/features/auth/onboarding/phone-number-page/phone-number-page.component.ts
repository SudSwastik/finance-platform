import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthCardComponent } from '../../../../shared/ui/auth-card/auth-card.component';

@Component({
  selector: 'app-phone-number-page',
  standalone: true,
  imports: [FormsModule, AuthCardComponent],
  templateUrl: './phone-number-page.component.html',
  styleUrl: './phone-number-page.component.scss',
})
export class PhoneNumberPageComponent {
  private readonly router = inject(Router);

  phone = '';

  onContinue(): void {
    this.router.navigate(['/register/onboarding/verify-2fa']);
  }
}
