export type AccountType = 'BANK' | 'CREDIT_CARD' | 'BROKERAGE' | 'CRYPTO_WALLET';

export interface AccountItem {
  id: string;
  type: AccountType;
  name: string;
  currency: string;
  balance: string;
}

export interface AccountDetail extends AccountItem {
  monthChange: string;
  moneyInMonth: string;
  moneyOutMonth: string;
  avgDailyMonth: string;
}
