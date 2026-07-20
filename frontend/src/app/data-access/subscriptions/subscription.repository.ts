import { Observable } from 'rxjs';
import { SubscriptionFilter, SubscriptionPage, SubscriptionStats } from '../../shared/models/subscription.models';

export abstract class SubscriptionRepository {
  abstract getPage(filter: SubscriptionFilter, page: number, size: number): Observable<SubscriptionPage>;
  abstract getStats(): Observable<SubscriptionStats>;
}
