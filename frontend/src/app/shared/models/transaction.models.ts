export type TransactionType = 'DEBIT' | 'CREDIT' | 'BUY' | 'SELL' | 'TRANSFER' | 'FEE';
export type TransactionStatus = 'PENDING' | 'SETTLED' | 'FAILED' | 'REVERSED';
export type TransactionTypeGroup = 'INCOME' | 'EXPENSE' | 'TRANSFERS';

export interface TransactionItem {
  id: string;
  merchantName: string | null;
  category: string | null;
  type: TransactionType;
  status: TransactionStatus;
  amount: string;
  transactionDate: string;
  accountName: string | null;
}

export interface TransactionPage {
  content: TransactionItem[];
  totalElements: number;
  page: number;
  size: number;
  totalPages: number;
}

export interface TransactionStats {
  moneyIn: string;
  moneyInCount: number;
  moneyOut: string;
  moneyOutCount: number;
  netFlow: string;
  totalCount: number;
}

export interface TransactionFilter {
  typeGroup?: TransactionTypeGroup;
  search?: string;
  month?: string;
  accountId?: string;
  category?: string;
  status?: TransactionStatus;
}
