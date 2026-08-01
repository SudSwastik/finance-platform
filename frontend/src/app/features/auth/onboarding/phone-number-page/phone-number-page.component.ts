import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthCardComponent } from '../../../../shared/ui/auth-card/auth-card.component';
import { AuthFacade } from '../../auth.facade';

@Component({
  selector: 'app-phone-number-page',
  standalone: true,
  imports: [FormsModule, AuthCardComponent],
  templateUrl: './phone-number-page.component.html',
  styleUrl: './phone-number-page.component.scss',
})
export class PhoneNumberPageComponent {
  private readonly router = inject(Router);
  private readonly facade = inject(AuthFacade);

  phone = '';

  onContinue(): void {
    this.facade.submitPhone(this.phone);
    this.router.navigate(['/register/onboarding/verify-2fa']);
  }
}
