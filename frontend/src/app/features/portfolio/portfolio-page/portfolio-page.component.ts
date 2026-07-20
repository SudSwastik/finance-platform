import { Component, inject, signal } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { shareReplay } from 'rxjs';
import { PortfolioRepository } from '../../../data-access/portfolio/portfolio.repository';
import { AssetType, HoldingItem, Trade } from '../../../shared/models/portfolio.models';

const TYPE_LABELS: Record<AssetType, string> = { STOCK: 'Stocks', CRYPTO: 'Crypto', ETF: 'ETFs' };
const TYPE_COLORS: Record<AssetType, string> = { STOCK: '#6E8FD6', CRYPTO: '#6E6E76', ETF: '#39393F' };

interface MixSegment {
  type: AssetType;
  label: string;
  color: string;
  value: number;
  percent: number;
}

@Component({
  selector: 'app-portfolio-page',
  standalone: true,
  imports: [AsyncPipe],
  templateUrl: './portfolio-page.component.html',
  styleUrl: './portfolio-page.component.scss',
})
export class PortfolioPageComponent {
  private readonly repo = inject(PortfolioRepository);

  readonly holdingsFilter = signal<'ALL' | AssetType>('ALL');

  readonly holdings$ = this.repo.listHoldings().pipe(shareReplay(1));
  readonly trades$ = this.repo.listTrades().pipe(shareReplay(1));

  setHoldingsFilter(filter: 'ALL' | AssetType): void {
    this.holdingsFilter.set(filter);
  }

  filteredHoldings(holdings: HoldingItem[]): HoldingItem[] {
    const filter = this.holdingsFilter();
    return filter === 'ALL' ? holdings : holdings.filter(h => h.assetType === filter);
  }

  totalValue(holdings: HoldingItem[]): number {
    return holdings.reduce((sum, h) => sum + parseFloat(h.value), 0);
  }

  totalCostBasis(holdings: HoldingItem[]): number {
    return holdings.reduce((sum, h) => sum + parseFloat(h.costBasis), 0);
  }

  totalReturn(holdings: HoldingItem[]): { amount: number; percent: number } {
    const value = this.totalValue(holdings);
    const cost = this.totalCostBasis(holdings);
    const amount = value - cost;
    return { amount, percent: cost !== 0 ? (amount / cost) * 100 : 0 };
  }

  mixSegments(holdings: HoldingItem[]): MixSegment[] {
    const total = this.totalValue(holdings);
    const byType = new Map<AssetType, number>();
    for (const h of holdings) {
      byType.set(h.assetType, (byType.get(h.assetType) ?? 0) + parseFloat(h.value));
    }
    return [...byType.entries()]
      .sort((a, b) => b[1] - a[1])
      .map(([type, value]) => ({
        type,
        label: TYPE_LABELS[type],
        color: TYPE_COLORS[type],
        value,
        percent: total !== 0 ? (value / total) * 100 : 0,
      }));
  }

  holdingTile(h: HoldingItem): { initials: string; icon: string | null; bg: string; color: string } {
    if (h.assetType === 'CRYPTO') {
      const icon = h.symbol === 'BTC' ? 'ph-currency-btc' : h.symbol === 'ETH' ? 'ph-currency-eth' : 'ph-coin';
      return { initials: '', icon, bg: '#6E8FD6', color: '#0C0C0F' };
    }
    return { initials: h.symbol.slice(0, 2).toUpperCase(), icon: null, bg: '#23232A', color: '#ECECEE' };
  }

  typeLabel(type: AssetType): string {
    return TYPE_LABELS[type];
  }

  isPositive(value: number): boolean {
    return value >= 0;
  }

  formatAmount(value: number, forceSign = false): string {
    const abs = Math.abs(value).toLocaleString('en-IN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
    if (value < 0) return `−₹${abs}`;
    return forceSign ? `+₹${abs}` : `₹${abs}`;
  }

  formatPercent(value: number): string {
    const sign = value >= 0 ? '+' : '−';
    return `${sign}${Math.abs(value).toFixed(1)}%`;
  }

  tradeAmountDisplay(t: Trade): string {
    const signed = t.side === 'SELL' ? parseFloat(t.amount) : -parseFloat(t.amount);
    return this.formatAmount(signed, true);
  }

  tradeSubline(t: Trade): string {
    const price = parseFloat(t.pricePerUnit).toLocaleString('en-IN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
    return `${t.quantity} ${t.assetSymbol} @ ₹${price}${t.accountName ? ' · ' + t.accountName : ''}`;
  }

  formatDate(dateStr: string): string {
    const [, m, d] = dateStr.split('-').map(Number);
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return `${months[m - 1]} ${d}`;
  }
}
