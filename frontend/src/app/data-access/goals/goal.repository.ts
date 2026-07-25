import { Observable } from 'rxjs';
import {
  ContributeToGoalRequest,
  CreateGoalRequest,
  GoalContribution,
  GoalItem,
  UpdateGoalRequest,
} from '../../shared/models/goal.models';

export abstract class GoalRepository {
  abstract list(): Observable<GoalItem[]>;
  abstract create(request: CreateGoalRequest): Observable<GoalItem>;
  abstract update(id: string, request: UpdateGoalRequest): Observable<GoalItem>;
  abstract delete(id: string): Observable<void>;
  abstract listContributions(goalId: string): Observable<GoalContribution[]>;
  abstract contribute(goalId: string, request: ContributeToGoalRequest): Observable<GoalItem>;
}
