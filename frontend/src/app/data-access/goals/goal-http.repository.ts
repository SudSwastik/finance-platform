import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { GoalRepository } from './goal.repository';
import {
  ContributeToGoalRequest,
  CreateGoalRequest,
  GoalContribution,
  GoalItem,
  UpdateGoalRequest,
} from '../../shared/models/goal.models';

@Injectable()
export class GoalHttpRepository extends GoalRepository {
  private readonly http = inject(HttpClient);

  private get headers(): HttpHeaders {
    return environment.devUserSub
      ? new HttpHeaders({ 'X-Dev-User-Sub': environment.devUserSub })
      : new HttpHeaders();
  }

  override list(): Observable<GoalItem[]> {
    return this.http.get<GoalItem[]>('/api/v1/goals', { headers: this.headers });
  }

  override create(request: CreateGoalRequest): Observable<GoalItem> {
    return this.http.post<GoalItem>('/api/v1/goals', request, { headers: this.headers });
  }

  override update(id: string, request: UpdateGoalRequest): Observable<GoalItem> {
    return this.http.patch<GoalItem>(`/api/v1/goals/${id}`, request, { headers: this.headers });
  }

  override delete(id: string): Observable<void> {
    return this.http.delete<void>(`/api/v1/goals/${id}`, { headers: this.headers });
  }

  override listContributions(goalId: string): Observable<GoalContribution[]> {
    return this.http.get<GoalContribution[]>(`/api/v1/goals/${goalId}/contributions`, { headers: this.headers });
  }

  override contribute(goalId: string, request: ContributeToGoalRequest): Observable<GoalItem> {
    return this.http.post<GoalItem>(`/api/v1/goals/${goalId}/contributions`, request, { headers: this.headers });
  }
}
