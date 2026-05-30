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

export interface OverviewData {
  budgets: TotalBudgetsSection;
  holdings: Holding[];
  recurring: RecurringBill[];
}
