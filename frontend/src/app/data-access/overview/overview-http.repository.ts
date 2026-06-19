import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OverviewRepository } from './overview.repository';
import { OverviewData } from '../../shared/models/overview.models';

@Injectable()
export class OverviewHttpRepository extends OverviewRepository {
  private readonly http = inject(HttpClient);

  override getOverview(): Observable<OverviewData> {
    throw new Error('OverviewHttpRepository.getOverview() not yet wired — set useMockData: true or complete chunk 2');
  }
}
