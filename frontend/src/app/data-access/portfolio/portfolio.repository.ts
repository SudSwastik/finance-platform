import { Observable } from 'rxjs';
import { HoldingItem, Trade } from '../../shared/models/portfolio.models';

export abstract class PortfolioRepository {
  abstract listHoldings(): Observable<HoldingItem[]>;
  abstract listTrades(): Observable<Trade[]>;
}
