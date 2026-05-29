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
  spending: {
    totalThisMonth: '350',
    points: [
      { date: '2025-07-06', thisMonth: '150', lastMonth: '200' },
      { date: '2025-07-07', thisMonth: '180', lastMonth: '220' },
      { date: '2025-07-08', thisMonth: '200', lastMonth: '250' },
      { date: '2025-07-09', thisMonth: '280', lastMonth: '300' },
      { date: '2025-07-10', thisMonth: '310', lastMonth: '320' },
      { date: '2025-07-11', thisMonth: '350', lastMonth: '400' },
      { date: '2025-07-12', thisMonth: '350', lastMonth: '410' },
    ],
  },
  goals: [
    { id: '1', name: 'Vacation', icon: '✈️', current: '2300', target: '3000', percent: 76.7, colorToken: 'brand.primary', targetDate: '2025-11-01' },
    { id: '2', name: 'House',    icon: '🏠', current: '5800', target: '10000', percent: 58,   colorToken: 'semantic.positive', targetDate: '2026-07-01' },
    { id: '3', name: 'Car',      icon: '🚗', current: '1450', target: '5000',  percent: 29,   colorToken: 'category.occasional', targetDate: '2026-03-01' },
  ],
  transactions: [
    { id: '1', merchant: 'Groceries', category: 'essential',  amount: '-128.45', date: '2025-07-11' },
    { id: '2', merchant: 'Gift',      category: 'occasional', amount: '-240.00', date: '2025-07-10' },
    { id: '3', merchant: 'Coffee',    category: 'lifestyle',  amount: '-200.00', date: '2025-07-08' },
  ],
  holdings: [
    { id: '1', symbol: 'AAPL', name: 'Apple',  costBasis: '1600.00', changePercent: 21.9,  value: '1950.00' },
    { id: '2', symbol: 'TSLA', name: 'Tesla',  costBasis: '2000.00', changePercent: 15.0,  value: '2300.00' },
    { id: '3', symbol: 'BTC',  name: 'Bitcoin', costBasis: '1100.00', changePercent: -19.1, value: '890.00' },
  ],
  recurring: [
    { id: '1', name: 'Spotify Premium',  frequency: 'monthly', amount: '10.99', nextDate: '2025-07-15' },
    { id: '2', name: 'ChatGPT Plus',     frequency: 'monthly', amount: '20.00', nextDate: '2025-07-18' },
    { id: '3', name: 'YouTube Premium',  frequency: 'monthly', amount: '11.99', nextDate: '2025-07-22' },
  ],
};

@Injectable()
export class OverviewMockRepository extends OverviewRepository {
  override getOverview(): Observable<OverviewData> {
    return of(MOCK_OVERVIEW);
  }
}
