import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthCardComponent } from '../../../../shared/ui/auth-card/auth-card.component';

@Component({
  selector: 'app-select-country-page',
  standalone: true,
  imports: [FormsModule, AuthCardComponent],
  templateUrl: './select-country-page.component.html',
  styleUrl: './select-country-page.component.scss',
})
export class SelectCountryPageComponent {
  private readonly router = inject(Router);

  country = 'US';

  readonly countries = [
    { code: 'US', name: 'United States', flag: '🇺🇸' },
    { code: 'GB', name: 'United Kingdom', flag: '🇬🇧' },
    { code: 'CA', name: 'Canada', flag: '🇨🇦' },
    { code: 'AU', name: 'Australia', flag: '🇦🇺' },
    { code: 'DE', name: 'Germany', flag: '🇩🇪' },
    { code: 'IN', name: 'India', flag: '🇮🇳' },
    { code: 'SG', name: 'Singapore', flag: '🇸🇬' },
  ];

  get selectedFlag(): string {
    return this.countries.find(c => c.code === this.country)?.flag ?? '🌐';
  }

  onContinue(): void {
    this.router.navigate(['/register/onboarding/phone']);
  }
}
