import { Observable } from 'rxjs';
import { TransactionFilter, TransactionPage, TransactionStats } from '../../shared/models/transaction.models';

export abstract class TransactionRepository {
  abstract getPage(filter: TransactionFilter, page: number, size: number): Observable<TransactionPage>;
  abstract getStats(month?: string): Observable<TransactionStats>;
}
