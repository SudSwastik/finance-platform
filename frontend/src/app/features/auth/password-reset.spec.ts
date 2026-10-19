import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter, Router } from '@angular/router';
import {
  CognitoIdentityProviderClient,
  ConfirmForgotPasswordCommand,
  ForgotPasswordCommand,
} from '@aws-sdk/client-cognito-identity-provider';
import { environment } from '../../../environments/environment';
import { AuthFacade } from './auth.facade';
import { ForgotPasswordPageComponent } from './forgot-password-page/forgot-password-page.component';
import { ForgotPasswordSuccessPageComponent } from './forgot-password-success-page/forgot-password-success-page.component';

describe('Password reset API', () => {
  beforeEach(() => TestBed.configureTestingModule({ providers: [provideHttpClient()] }));

  it('requests a code from Cognito for the trimmed email', async () => {
    const send = spyOn(CognitoIdentityProviderClient.prototype, 'send')
      .and.returnValue(Promise.resolve({}) as never);
    const facade = TestBed.inject(AuthFacade);
    await facade.forgotPassword(' alice@example.com ');
    const command = send.calls.mostRecent().args[0] as ForgotPasswordCommand;
    expect(command instanceof ForgotPasswordCommand).toBeTrue();
    expect(command.input).toEqual({ ClientId: environment.cognito.clientId, Username: 'alice@example.com' });
    expect(facade.passwordResetEmail).toBe('alice@example.com');
  });

  it('submits the code and exact new password to Cognito', async () => {
    const send = spyOn(CognitoIdentityProviderClient.prototype, 'send')
      .and.returnValue(Promise.resolve({}) as never);
    const facade = TestBed.inject(AuthFacade);
    await facade.confirmForgotPassword(' alice@example.com ', ' 123456 ', ' NewPassword1! ');
    const command = send.calls.mostRecent().args[0] as ConfirmForgotPasswordCommand;
    expect(command instanceof ConfirmForgotPasswordCommand).toBeTrue();
    expect(command.input).toEqual({
      ClientId: environment.cognito.clientId,
      Username: 'alice@example.com',
      ConfirmationCode: '123456',
      Password: ' NewPassword1! ',
    });
  });
});

describe('Password reset screens', () => {
  let facade: jasmine.SpyObj<AuthFacade>;
  let navigate: jasmine.Spy;

  beforeEach(() => {
    facade = jasmine.createSpyObj<AuthFacade>('AuthFacade', ['forgotPassword', 'confirmForgotPassword'], {
      passwordResetEmail: 'alice@example.com',
    });
    TestBed.configureTestingModule({
      imports: [ForgotPasswordPageComponent, ForgotPasswordSuccessPageComponent],
      providers: [provideRouter([]), { provide: AuthFacade, useValue: facade }],
    });
    navigate = spyOn(TestBed.inject(Router), 'navigate').and.resolveTo(true);
  });

  it('waits for code delivery and prevents duplicate requests', async () => {
    let finish!: () => void;
    facade.forgotPassword.and.returnValue(new Promise<void>(resolve => finish = resolve));
    const fixture = TestBed.createComponent(ForgotPasswordPageComponent);
    const page = fixture.componentInstance;
    page.email = 'alice@example.com';
    const request = page.onSend();
    await page.onSend();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.auth-card__cta').disabled).toBeTrue();
    expect(navigate).not.toHaveBeenCalled();
    expect(facade.forgotPassword).toHaveBeenCalledTimes(1);
    finish();
    await request;
    expect(navigate).toHaveBeenCalledWith(['/forgot-password/success']);
  });

  it('shows a delivery error instead of navigating to success', async () => {
    facade.forgotPassword.and.rejectWith(new Error('Unable to deliver code'));
    const fixture = TestBed.createComponent(ForgotPasswordPageComponent);
    fixture.componentInstance.email = 'alice@example.com';
    await fixture.componentInstance.onSend();
    fixture.detectChanges();
    expect(navigate).not.toHaveBeenCalled();
    expect(fixture.nativeElement.querySelector('[role="alert"]').textContent).toContain('Unable to deliver code');
    expect(fixture.componentInstance.submitting).toBeFalse();
  });

  it('rejects invalid email before contacting Cognito', async () => {
    const page = TestBed.createComponent(ForgotPasswordPageComponent).componentInstance;
    page.email = 'invalid';
    await page.onSend();
    expect(facade.forgotPassword).not.toHaveBeenCalled();
    expect(navigate).not.toHaveBeenCalled();
  });

  it('validates matching passwords and allows retry after an expired code', async () => {
    const fixture = TestBed.createComponent(ForgotPasswordSuccessPageComponent);
    const page = fixture.componentInstance;
    page.code = '123456';
    page.password = 'NewPassword1!';
    page.confirmPassword = 'different';
    await page.onContinue();
    expect(facade.confirmForgotPassword).not.toHaveBeenCalled();
    expect(page.errorMessage).toBe('Passwords do not match.');

    page.confirmPassword = page.password;
    facade.confirmForgotPassword.and.rejectWith(new Error('Code has expired'));
    await page.onContinue();
    fixture.detectChanges();
    expect(page.completed).toBeFalse();
    expect(fixture.nativeElement.querySelector('[role="alert"]').textContent).toContain('Code has expired');

    facade.confirmForgotPassword.and.resolveTo();
    await page.onContinue();
    fixture.detectChanges();
    expect(facade.confirmForgotPassword).toHaveBeenCalledWith('alice@example.com', '123456', 'NewPassword1!');
    expect(page.completed).toBeTrue();
    expect(page.password).toBe('');
    expect(fixture.nativeElement.querySelector('input')).toBeNull();
    await page.onContinue();
    expect(navigate).toHaveBeenCalledWith(['/login']);
  });
});
