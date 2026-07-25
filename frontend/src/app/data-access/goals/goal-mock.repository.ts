import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { GoalRepository } from './goal.repository';
import {
  ContributeToGoalRequest,
  CreateGoalRequest,
  GoalContribution,
  GoalItem,
  UpdateGoalRequest,
} from '../../shared/models/goal.models';

const COLOR_PALETTE = ['goal.primary', 'goal.positive', 'goal.warning', 'goal.neutral'];

function daysAgoIso(days: number): string {
  const d = new Date();
  d.setDate(d.getDate() - days);
  return d.toISOString();
}

function addMoney(a: string, b: string): string {
  return (parseFloat(a) + parseFloat(b)).toFixed(2);
}

function computePercent(current: string, target: string): number {
  const targetValue = parseFloat(target);
  if (targetValue === 0) return 0;
  return Math.round((parseFloat(current) / targetValue) * 1000) / 10;
}

const INITIAL_GOALS: GoalItem[] = [
  { id: '1', name: 'Emergency Fund', current: '15600.00', target: '20000.00', percent: 78, colorToken: 'goal.positive', targetDate: '2026-12-15' },
  { id: '2', name: 'House Down Payment', current: '42000.00', target: '100000.00', percent: 42, colorToken: 'goal.primary', targetDate: '2028-06-15' },
  { id: '3', name: 'Vacation - Japan', current: '4500.00', target: '5000.00', percent: 90, colorToken: 'goal.neutral', targetDate: '2026-08-15' },
];

const INITIAL_CONTRIBUTIONS: Record<string, GoalContribution[]> = {
  '1': [{ id: 'c1', goalId: '1', amount: '500.00', note: 'Monthly auto-deposit', contributedAt: daysAgoIso(10) }],
  '2': [{ id: 'c2', goalId: '2', amount: '2000.00', note: 'Monthly auto-deposit', contributedAt: daysAgoIso(6) }],
  '3': [{ id: 'c3', goalId: '3', amount: '250.00', note: 'Monthly auto-deposit', contributedAt: daysAgoIso(3) }],
};

@Injectable()
export class GoalMockRepository extends GoalRepository {
  private readonly goalsSubject = new BehaviorSubject<GoalItem[]>(INITIAL_GOALS.map(g => ({ ...g })));
  private readonly contributions = new Map<string, GoalContribution[]>(
    Object.entries(INITIAL_CONTRIBUTIONS).map(([id, list]) => [id, list.map(c => ({ ...c }))]),
  );
  private nextGoalId = INITIAL_GOALS.length + 1;
  private nextContributionId = 4;

  override list(): Observable<GoalItem[]> {
    return this.goalsSubject.asObservable();
  }

  override create(request: CreateGoalRequest): Observable<GoalItem> {
    const existing = this.goalsSubject.value;
    const colorToken = request.colorToken?.trim() || COLOR_PALETTE[existing.length % COLOR_PALETTE.length];
    const goal: GoalItem = {
      id: String(this.nextGoalId++),
      name: request.name,
      current: '0.00',
      target: request.target,
      percent: 0,
      colorToken,
      targetDate: request.targetDate,
    };
    this.goalsSubject.next([...existing, goal]);
    this.contributions.set(goal.id, []);
    return of(goal);
  }

  override update(id: string, request: UpdateGoalRequest): Observable<GoalItem> {
    const existing = this.goalsSubject.value;
    if (!existing.some(g => g.id === id)) {
      return throwError(() => new Error(`Goal not found: ${id}`));
    }
    const updated = existing.map(g => {
      if (g.id !== id) return g;
      const target = request.target ?? g.target;
      return {
        ...g,
        name: request.name ?? g.name,
        target,
        targetDate: request.targetDate ?? g.targetDate,
        percent: computePercent(g.current, target),
      };
    });
    this.goalsSubject.next(updated);
    return of(updated.find(g => g.id === id)!);
  }

  override delete(id: string): Observable<void> {
    this.goalsSubject.next(this.goalsSubject.value.filter(g => g.id !== id));
    this.contributions.delete(id);
    return of(void 0);
  }

  override listContributions(goalId: string): Observable<GoalContribution[]> {
    const list = this.contributions.get(goalId) ?? [];
    return of([...list].sort((a, b) => b.contributedAt.localeCompare(a.contributedAt)));
  }

  override contribute(goalId: string, request: ContributeToGoalRequest): Observable<GoalItem> {
    const existing = this.goalsSubject.value;
    if (!existing.some(g => g.id === goalId)) {
      return throwError(() => new Error(`Goal not found: ${goalId}`));
    }
    const contribution: GoalContribution = {
      id: 'c' + this.nextContributionId++,
      goalId,
      amount: request.amount,
      note: request.note?.trim() || null,
      contributedAt: new Date().toISOString(),
    };
    this.contributions.set(goalId, [...(this.contributions.get(goalId) ?? []), contribution]);

    const updated = existing.map(g => {
      if (g.id !== goalId) return g;
      const current = addMoney(g.current, request.amount);
      return { ...g, current, percent: computePercent(current, g.target) };
    });
    this.goalsSubject.next(updated);
    return of(updated.find(g => g.id === goalId)!);
  }
}
