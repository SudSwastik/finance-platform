import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthCardComponent } from '../../../../shared/ui/auth-card/auth-card.component';
import { AuthFacade } from '../../auth.facade';

@Component({
  selector: 'app-account-type-page',
  standalone: true,
  imports: [FormsModule, AuthCardComponent],
  templateUrl: './account-type-page.component.html',
  styleUrl: './account-type-page.component.scss',
})
export class AccountTypePageComponent {
  private readonly router = inject(Router);
  private readonly facade = inject(AuthFacade);

  accountType: 'personal' | 'business' = 'personal';

  onContinue(): void {
    this.facade.submitAccountType(this.accountType);
    this.router.navigate(['/register/onboarding/country']);
  }
}
