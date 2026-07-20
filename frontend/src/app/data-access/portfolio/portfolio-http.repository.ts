import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PortfolioRepository } from './portfolio.repository';
import { HoldingItem, Trade } from '../../shared/models/portfolio.models';

@Injectable()
export class PortfolioHttpRepository extends PortfolioRepository {
  private readonly http = inject(HttpClient);

  private get headers(): HttpHeaders {
    return environment.devUserSub
      ? new HttpHeaders({ 'X-Dev-User-Sub': environment.devUserSub })
      : new HttpHeaders();
  }

  override listHoldings(): Observable<HoldingItem[]> {
    return this.http.get<HoldingItem[]>('/api/v1/portfolio/holdings', {
      headers: this.headers,
    });
  }

  override listTrades(): Observable<Trade[]> {
    return this.http.get<Trade[]>('/api/v1/finance/trades', {
      headers: this.headers,
    });
  }
}
