import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthFacade } from '../auth.facade';
import { AuthShellComponent } from '../auth-shell/auth-shell.component';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [FormsModule, AuthShellComponent],
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.scss',
})
export class RegisterPageComponent {
  private readonly facade = inject(AuthFacade);

  name = '';
  email = '';
  readonly password = signal('');
  showPassword = false;

  readonly strength = computed(() => {
    const p = this.password();
    if (!p) return 0;
    let score = 0;
    if (p.length >= 8) score++;
    if (/[A-Z]/.test(p)) score++;
    if (/[0-9]/.test(p)) score++;
    return score;
  });

  readonly strengthLabel = computed(() => ['', 'Weak', 'Medium', 'Strong'][this.strength()]);

  onPasswordChange(value: string): void {
    this.password.set(value);
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    this.facade.register({ name: this.name, email: this.email, password: this.password() });
  }
}
