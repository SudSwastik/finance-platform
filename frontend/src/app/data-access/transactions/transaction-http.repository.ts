import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TransactionRepository } from './transaction.repository';
import {
  TransactionFilter,
  TransactionPage,
  TransactionStats,
} from '../../shared/models/transaction.models';

@Injectable()
export class TransactionHttpRepository extends TransactionRepository {
  private readonly http = inject(HttpClient);

  private get headers(): HttpHeaders {
    return environment.devUserSub
      ? new HttpHeaders({ 'X-Dev-User-Sub': environment.devUserSub })
      : new HttpHeaders();
  }

  override getPage(filter: TransactionFilter, page: number, size: number): Observable<TransactionPage> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (filter.typeGroup) params = params.set('typeGroup', filter.typeGroup);
    if (filter.search)    params = params.set('search', filter.search);
    if (filter.month)     params = params.set('month', filter.month);
    if (filter.accountId) params = params.set('accountId', filter.accountId);
    if (filter.category)  params = params.set('category', filter.category);
    if (filter.status)    params = params.set('status', filter.status);

    return this.http.get<TransactionPage>('/api/v1/finance/transactions', {
      headers: this.headers,
      params,
    });
  }

  override getStats(month?: string): Observable<TransactionStats> {
    let params = new HttpParams();
    if (month) params = params.set('month', month);

    return this.http.get<TransactionStats>('/api/v1/finance/transactions/stats', {
      headers: this.headers,
      params,
    });
  }
}
