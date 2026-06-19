import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OverviewRepository } from './overview.repository';
import { OverviewData, Holding, RecurringBill, BudgetCategory } from '../../shared/models/overview.models';

// BFF response shape — mirrors OverviewResponseDto + nested DTOs
interface BffBudgetCategory {
  id: string;
  name: string;
  colorToken: string;
  spent: string;
  budget: string;
  percentUsed: number;
}

interface BffTotalBudgets {
  total: string;
  filter: string;
  categories: BffBudgetCategory[];
}

interface BffOverviewResponse {
  totalBudgets: BffTotalBudgets;
  investments: Holding[];
  recurring: RecurringBill[]; 
}

const MOCK_RECURRING: RecurringBill[] = [
  { id: '1', name: 'Spotify Premium',  frequency: 'monthly', amount: '10.99', nextDate: '2025-07-15' },
  { id: '2', name: 'ChatGPT Plus',     frequency: 'monthly', amount: '20.00', nextDate: '2025-07-18' },
  { id: '3', name: 'YouTube Premium',  frequency: 'monthly', amount: '11.99', nextDate: '2025-07-22' },
];

@Injectable()
export class OverviewHttpRepository extends OverviewRepository {
  private readonly http = inject(HttpClient);

  override getOverview(): Observable<OverviewData> {
    const headers = environment.devUserSub
      ? new HttpHeaders({ 'X-Dev-User-Sub': environment.devUserSub })
      : new HttpHeaders();

    return this.http
      .get<BffOverviewResponse>('/api/v1/dashboard/overview', { headers })
      .pipe(map(res => this.mapToOverviewData(res)));
  }

  private mapToOverviewData(res: BffOverviewResponse): OverviewData {
    const categories: BudgetCategory[] = res.totalBudgets.categories.map(c => ({
      id: c.id,
      name: c.name,
      colorToken: c.colorToken,
      spent: c.spent,
      budget: c.budget,
      percentUsed: c.percentUsed,
    }));

    return {
      budgets: {
        totalDisplay: res.totalBudgets.total,
        filterLabel: res.totalBudgets.filter,
        categories,
      },
      holdings: res.investments,
      recurring: MOCK_RECURRING,
    };
  }
}
