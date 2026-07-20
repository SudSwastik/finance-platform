import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { SubscriptionsPageComponent } from './subscriptions-page.component';
import { SubscriptionRepository } from '../../../data-access/subscriptions/subscription.repository';
import { SubscriptionPage, SubscriptionStats } from '../../../shared/models/subscription.models';

class FakeSubscriptionRepository extends SubscriptionRepository {
  override getPage() {
    return of<SubscriptionPage>({ content: [], totalElements: 0, page: 0, size: 20, totalPages: 0 });
  }

  override getStats() {
    return of<SubscriptionStats>({ activeCount: 0, monthlyCost: '0.00', yearlyCost: '0.00', nextRenewal: null });
  }
}

describe('SubscriptionsPageComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubscriptionsPageComponent],
      providers: [{ provide: SubscriptionRepository, useClass: FakeSubscriptionRepository }],
    }).compileComponents();
  });

  it('renders the .subscriptions-page root class', () => {
    const fixture = TestBed.createComponent(SubscriptionsPageComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.subscriptions-page')).toBeTruthy();
  });
});
