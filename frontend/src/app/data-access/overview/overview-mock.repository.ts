import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { OverviewRepository } from './overview.repository';
import { OverviewData } from '../../shared/models/overview.models';

// Seed data aligned with docs/api/seed-users.md
const MOCK_OVERVIEW: OverviewData = {
  budgets: {
    totalDisplay: '6400',
    filterLabel: 'Expenses',
    categories: [
      { id: '1', name: 'Essentials',  colorToken: 'essentials',  spent: '1750', budget: '2800', percentUsed: 62.5 },
      { id: '2', name: 'Lifestyles',  colorToken: 'lifestyles',  spent: '900',  budget: '2000', percentUsed: 45.0 },
      { id: '3', name: 'Occasional',  colorToken: 'occasional',  spent: '1170', budget: '1600', percentUsed: 73.1 },
      { id: '4', name: 'Others',      colorToken: 'others',      spent: '1300', budget: '2000', percentUsed: 65.0 },
    ],
  },
  holdings: [
    { id: '1', symbol: 'AAPL', name: 'Apple',   costBasis: '1600.00', changePercent: 21.9,  value: '1950.00' },
    { id: '2', symbol: 'TSLA', name: 'Tesla',   costBasis: '2000.00', changePercent: 15.0,  value: '2300.00' },
    { id: '3', symbol: 'BTC',  name: 'Bitcoin', costBasis: '1100.00', changePercent: -19.1, value: '890.00' },
  ],
  recurring: [
    { id: '1', name: 'Spotify Premium',  frequency: 'monthly', amount: '10.99', nextDate: '2025-07-15' },
    { id: '2', name: 'ChatGPT Plus',     frequency: 'monthly', amount: '20.00', nextDate: '2025-07-18' },
    { id: '3', name: 'YouTube Premium',  frequency: 'monthly', amount: '11.99', nextDate: '2025-07-22' },
  ],
  recentTransactions: [
    { id: '1', merchantName: 'Salary Credit',   category: 'Income',    type: 'CREDIT', amount: '85000.00', transactionDate: '2026-06-01' },
    { id: '2', merchantName: 'Rent',            category: 'Housing',   type: 'DEBIT',  amount: '22000.00', transactionDate: '2026-06-05' },
    { id: '3', merchantName: 'Zepto',           category: 'Groceries', type: 'DEBIT',  amount: '1284.00',  transactionDate: '2026-06-10' },
    { id: '4', merchantName: 'Swiggy',          category: 'Food',      type: 'DEBIT',  amount: '450.00',   transactionDate: '2026-06-12' },
    { id: '5', merchantName: 'BTC Buy',         category: 'Crypto',    type: 'BUY',    amount: '5000.00',  transactionDate: '2026-06-15' },
  ],
  monthlySummary: {
    income: '85000.00',
    spending: '45000.00',
    netSaved: '40000.00',
  },
};

@Injectable()
export class OverviewMockRepository extends OverviewRepository {
  override getOverview(): Observable<OverviewData> {
    return of(MOCK_OVERVIEW);
  }
}
