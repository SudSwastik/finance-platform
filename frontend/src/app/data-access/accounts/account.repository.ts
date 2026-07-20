import { Observable } from 'rxjs';
import { AccountDetail, AccountItem } from '../../shared/models/account.models';

export abstract class AccountRepository {
  abstract list(): Observable<AccountItem[]>;
  abstract getById(id: string): Observable<AccountDetail>;
}
