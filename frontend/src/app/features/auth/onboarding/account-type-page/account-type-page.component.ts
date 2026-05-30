import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthCardComponent } from '../../../../shared/ui/auth-card/auth-card.component';

@Component({
  selector: 'app-account-type-page',
  standalone: true,
  imports: [FormsModule, AuthCardComponent],
  templateUrl: './account-type-page.component.html',
  styleUrl: './account-type-page.component.scss',
})
export class AccountTypePageComponent {
  private readonly router = inject(Router);

  accountType: 'personal' | 'business' = 'personal';

  onContinue(): void {
    this.router.navigate(['/register/onboarding/country']);
  }
}
