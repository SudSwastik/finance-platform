export type RecurringFrequency = 'WEEKLY' | 'MONTHLY' | 'YEARLY';

export interface SubscriptionItem {
  id: string;
  name: string;
  category: string | null;
  amount: string;
  currency: string;
  frequency: RecurringFrequency | null;
  nextDueDate: string | null;
  status: string;
  accountName: string | null;
}

export interface SubscriptionPage {
  content: SubscriptionItem[];
  totalElements: number;
  page: number;
  size: number;
  totalPages: number;
}

export interface SubscriptionStats {
  activeCount: number;
  monthlyCost: string;
  yearlyCost: string;
  nextRenewal: string | null;
}

export interface SubscriptionFilter {
  search?: string;
  category?: string;
  frequency?: RecurringFrequency;
  status?: string;
}
