import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AccountRepository } from './account.repository';
import { AccountDetail, AccountItem } from '../../shared/models/account.models';

@Injectable()
export class AccountHttpRepository extends AccountRepository {
  private readonly http = inject(HttpClient);

  private get headers(): HttpHeaders {
    return environment.devUserSub
      ? new HttpHeaders({ 'X-Dev-User-Sub': environment.devUserSub })
      : new HttpHeaders();
  }

  override list(): Observable<AccountItem[]> {
    return this.http.get<AccountItem[]>('/api/v1/finance/accounts', {
      headers: this.headers,
    });
  }

  override getById(id: string): Observable<AccountDetail> {
    return this.http.get<AccountDetail>(`/api/v1/finance/accounts/${id}`, {
      headers: this.headers,
    });
  }
}
