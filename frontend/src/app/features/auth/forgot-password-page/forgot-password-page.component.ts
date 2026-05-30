import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthShellComponent } from '../auth-shell/auth-shell.component';
import { AuthCardComponent } from '../../../shared/ui/auth-card/auth-card.component';
import { AuthFacade } from '../auth.facade';

@Component({
  selector: 'app-forgot-password-page',
  standalone: true,
  imports: [FormsModule, AuthShellComponent, AuthCardComponent],
  templateUrl: './forgot-password-page.component.html',
  styleUrl: './forgot-password-page.component.scss',
})
export class ForgotPasswordPageComponent {
  private readonly router = inject(Router);
  private readonly facade = inject(AuthFacade);

  email = '';

  onSend(): void {
    this.facade.forgotPassword(this.email);
    this.router.navigate(['/forgot-password/success']);
  }
}
