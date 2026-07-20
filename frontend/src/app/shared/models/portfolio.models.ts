export type AssetType = 'STOCK' | 'CRYPTO' | 'ETF';
export type TradeSide = 'BUY' | 'SELL';

export interface HoldingItem {
  id: string;
  symbol: string;
  name: string;
  assetType: AssetType;
  costBasis: string;
  changePercent: number;
  value: string;
}

export interface Trade {
  id: string;
  side: TradeSide;
  assetSymbol: string;
  assetName: string;
  quantity: string;
  pricePerUnit: string;
  amount: string;
  accountName: string | null;
  tradeDate: string;
}
