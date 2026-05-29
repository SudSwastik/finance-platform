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

export interface SpendingPoint {
  date: string;
  thisMonth: Money;
  lastMonth: Money;
}

export interface SpendingSection {
  totalThisMonth: Money;
  points: SpendingPoint[];
}

export interface Goal {
  id: string;
  name: string;
  icon: string;
  current: Money;
  target: Money;
  percent: number;
  colorToken: string;
  targetDate: string;
}

export interface Transaction {
  id: string;
  merchant: string;
  category: 'essential' | 'lifestyle' | 'occasional' | 'other';
  amount: Money;
  date: string;
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
  spending: SpendingSection;
  goals: Goal[];
  transactions: Transaction[];
  holdings: Holding[];
  recurring: RecurringBill[];
}
