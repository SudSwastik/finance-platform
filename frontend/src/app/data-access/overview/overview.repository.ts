import { Observable } from 'rxjs';
import { OverviewData } from '../../shared/models/overview.models';

export abstract class OverviewRepository {
  abstract getOverview(): Observable<OverviewData>;
}
