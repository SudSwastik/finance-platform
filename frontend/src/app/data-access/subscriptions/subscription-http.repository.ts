import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SubscriptionRepository } from './subscription.repository';
import {
  SubscriptionFilter,
  SubscriptionPage,
  SubscriptionStats,
} from '../../shared/models/subscription.models';

@Injectable()
export class SubscriptionHttpRepository extends SubscriptionRepository {
  private readonly http = inject(HttpClient);

  private get headers(): HttpHeaders {
    return environment.devUserSub
      ? new HttpHeaders({ 'X-Dev-User-Sub': environment.devUserSub })
      : new HttpHeaders();
  }

  override getPage(filter: SubscriptionFilter, page: number, size: number): Observable<SubscriptionPage> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (filter.search)    params = params.set('search', filter.search);
    if (filter.category)  params = params.set('category', filter.category);
    if (filter.frequency) params = params.set('frequency', filter.frequency);
    if (filter.status)    params = params.set('status', filter.status);

    return this.http.get<SubscriptionPage>('/api/v1/finance/subscriptions', {
      headers: this.headers,
      params,
    });
  }

  override getStats(): Observable<SubscriptionStats> {
    return this.http.get<SubscriptionStats>('/api/v1/finance/subscriptions/stats', {
      headers: this.headers,
    });
  }
}
