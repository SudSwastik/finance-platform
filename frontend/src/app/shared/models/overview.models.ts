export type Money = string; // ISO decimal string e.g. "128.45"

export interface BudgetCategory {
  id: string;
  name: string;
  colorToken: string;
  spent: Money;
  budget: Money;
  percentUsed: number;
}

export interface TotalBudgetsSection {
  totalDisplay: Money;
  filterLabel: string;
  categories: BudgetCategory[];
}

export interface Holding {
  id: string;
  symbol: string;
  name: string;
  costBasis: Money;
  changePercent: number;
  value: Money;
}

export interface RecurringBill {
  id: string;
  name: string;
  frequency: 'monthly' | 'yearly' | 'weekly';
  amount: Money;
  nextDate: string;
}

export interface RecentTransaction {
  id: string;
  merchantName: string;
  category: string;
  type: 'DEBIT' | 'CREDIT' | 'BUY' | 'SELL' | 'TRANSFER' | 'FEE';
  amount: Money;
  transactionDate: string;
}

export interface MonthlySummary {
  income: Money;
  spending: Money;
  netSaved: Money;
}

export interface NetWorthData {
  total: Money;
  changePercent: number;
  vsLastMonth: Money;
  // 12 monthly y-values in SVG space (0 = top, 180 = bottom) Jul → Jun
  chartY: number[];
}

export interface OverviewData {
  netWorth: NetWorthData;
  budgets: TotalBudgetsSection;
  holdings: Holding[];
  recurring: RecurringBill[];
  recentTransactions: RecentTransaction[];
  monthlySummary: MonthlySummary;
}
