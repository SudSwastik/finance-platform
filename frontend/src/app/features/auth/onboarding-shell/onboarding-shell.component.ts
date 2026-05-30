import { Component, inject } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd, ActivatedRoute } from '@angular/router';
import { filter, map, merge, of } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';

interface OnboardingStep {
  num: number;
  label: string;
}

@Component({
  selector: 'app-onboarding-shell',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './onboarding-shell.component.html',
  styleUrl: './onboarding-shell.component.scss',
})
export class OnboardingShellComponent {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly steps: OnboardingStep[] = [
    { num: 1, label: 'Email' },
    { num: 2, label: 'Account Type' },
    { num: 3, label: 'Country' },
    { num: 4, label: '2FA' },
  ];

  readonly currentStep = toSignal(
    merge(
      of(null),
      this.router.events.pipe(filter(e => e instanceof NavigationEnd))
    ).pipe(map(() => (this.route.firstChild?.snapshot.data['step'] as number) ?? 2)),
    { initialValue: (this.route.firstChild?.snapshot.data['step'] as number) ?? 2 }
  );

  stepState(num: number): 'done' | 'active' | 'pending' {
    const step = this.currentStep();
    if (num < step) return 'done';
    if (num === step) return 'active';
    return 'pending';
  }
}
