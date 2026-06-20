import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OverviewRepository } from './overview.repository';
import { OverviewData, Holding, RecurringBill, BudgetCategory, RecentTransaction, MonthlySummary, NetWorthData } from '../../shared/models/overview.models';

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

interface BffRecurringItem {
  id: string | null;
  name: string;
  frequency: string;
  amount: string;
  nextDate: string;
}

interface BffOverviewResponse {
  totalBudgets: BffTotalBudgets;
  investments: Holding[];
  recurring: BffRecurringItem[];
  recentTransactions: RecentTransaction[];
  monthlySummary: MonthlySummary;
  netWorth?: NetWorthData;
}

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
      netWorth: res.netWorth ?? {
        total: '0.00',
        changePercent: 0,
        vsLastMonth: '0.00',
        chartY: [180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180],
      },
      budgets: {
        totalDisplay: res.totalBudgets.total,
        filterLabel: res.totalBudgets.filter,
        categories,
      },
      holdings: res.investments,
      recurring: res.recurring.map((r, i) => ({
        id: r.id ?? String(i),
        name: r.name,
        frequency: r.frequency.toLowerCase() as RecurringBill['frequency'],
        amount: r.amount,
        nextDate: r.nextDate,
      })),
      recentTransactions: res.recentTransactions,
      monthlySummary: res.monthlySummary,
    };
  }
}
