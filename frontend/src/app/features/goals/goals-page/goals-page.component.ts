import { Component, TemplateRef, ViewChild, inject, signal } from '@angular/core';
import { AsyncPipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, forkJoin, map, of, shareReplay, switchMap } from 'rxjs';
import { GoalRepository } from '../../../data-access/goals/goal.repository';
import { ModalService } from '../../../shared/ui/modal/modal.service';
import { GoalContribution, GoalItem } from '../../../shared/models/goal.models';

interface GoalColors {
  ring: string;
  iconBg: string;
  iconColor: string;
}

const GOAL_COLORS: Record<string, GoalColors> = {
  'goal.primary': { ring: '#6E8FD6', iconBg: '#141821', iconColor: '#6E8FD6' },
  'goal.positive': { ring: '#4FAE85', iconBg: '#16271F', iconColor: '#4FAE85' },
  'goal.warning': { ring: '#C9A24B', iconBg: '#29230F', iconColor: '#C9A24B' },
  'goal.neutral': { ring: '#A8A8AE', iconBg: '#1C1C21', iconColor: '#A8A8AE' },
};
const DEFAULT_COLORS = GOAL_COLORS['goal.neutral'];

const RING_RADIUS = 42;
const RING_CIRCUMFERENCE = 2 * Math.PI * RING_RADIUS;

type FormMode = 'create' | 'edit';

@Component({
  selector: 'app-goals-page',
  standalone: true,
  imports: [AsyncPipe, DecimalPipe, FormsModule],
  templateUrl: './goals-page.component.html',
  styleUrl: './goals-page.component.scss',
})
export class GoalsPageComponent {
  private readonly repo = inject(GoalRepository);
  private readonly modalService = inject(ModalService);

  @ViewChild('goalFormTpl') private goalFormTpl!: TemplateRef<unknown>;
  @ViewChild('contributeFormTpl') private contributeFormTpl!: TemplateRef<unknown>;

  private readonly refresh$ = new BehaviorSubject<void>(undefined);

  readonly goals$ = this.refresh$.pipe(
    switchMap(() => this.repo.list()),
    shareReplay(1),
  );

  readonly allContributions$ = this.goals$.pipe(
    switchMap(goals =>
      goals.length ? forkJoin(goals.map(g => this.repo.listContributions(g.id))) : of([] as GoalContribution[][]),
    ),
    map(lists => lists.flat()),
    shareReplay(1),
  );

  readonly openMenuId = signal<string | null>(null);

  formMode: FormMode = 'create';
  editingGoalId: string | null = null;
  goalName = '';
  goalTarget = '';
  goalTargetDate = '';

  contributingGoalId: string | null = null;
  contributingGoalName = '';
  contributeAmount = '';
  contributeNote = '';

  ringCircumference = RING_CIRCUMFERENCE;

  colorsFor(colorToken: string): GoalColors {
    return GOAL_COLORS[colorToken] ?? DEFAULT_COLORS;
  }

  ringOffset(percent: number): number {
    const clamped = Math.min(Math.max(percent, 0), 100);
    return RING_CIRCUMFERENCE * (1 - clamped / 100);
  }

  badge(percent: number): { label: string; icon: string } {
    return percent >= 90 ? { label: 'Almost there', icon: 'ph-confetti' } : { label: 'On track', icon: 'ph-check' };
  }

  totalSaved(goals: GoalItem[]): number {
    return goals.reduce((sum, g) => sum + parseFloat(g.current), 0);
  }

  totalTarget(goals: GoalItem[]): number {
    return goals.reduce((sum, g) => sum + parseFloat(g.target), 0);
  }

  combinedProgress(goals: GoalItem[]): number {
    const target = this.totalTarget(goals);
    return target === 0 ? 0 : (this.totalSaved(goals) / target) * 100;
  }

  contributedThisMonth(contributions: GoalContribution[]): number {
    const now = new Date();
    return contributions
      .filter(c => {
        const d = new Date(c.contributedAt);
        return d.getFullYear() === now.getFullYear() && d.getMonth() === now.getMonth();
      })
      .reduce((sum, c) => sum + parseFloat(c.amount), 0);
  }

  formatAmount(value: number): string {
    const formatted = value.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    return `₹${formatted}`;
  }

  formatMoneyString(value: string): string {
    return this.formatAmount(parseFloat(value));
  }

  formatDate(dateStr: string): string {
    const [y, m] = dateStr.split('-').map(Number);
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    return `${months[m - 1]} ${y}`;
  }

  toggleMenu(goalId: string): void {
    this.openMenuId.set(this.openMenuId() === goalId ? null : goalId);
  }

  openNewGoalModal(): void {
    this.formMode = 'create';
    this.editingGoalId = null;
    this.goalName = '';
    this.goalTarget = '';
    this.goalTargetDate = '';

    this.modalService
      .open({ title: 'New goal', confirmLabel: 'Create', bodyTemplate: this.goalFormTpl })
      .subscribe(confirmed => {
        if (!confirmed) return;
        if (!this.goalName.trim() || !this.goalTarget || !this.goalTargetDate) return;
        this.repo
          .create({ name: this.goalName.trim(), target: this.goalTarget, targetDate: this.goalTargetDate })
          .subscribe(() => this.refresh$.next());
      });
  }

  openEditGoalModal(goal: GoalItem): void {
    this.openMenuId.set(null);
    this.formMode = 'edit';
    this.editingGoalId = goal.id;
    this.goalName = goal.name;
    this.goalTarget = goal.target;
    this.goalTargetDate = goal.targetDate;

    this.modalService
      .open({ title: 'Edit goal', confirmLabel: 'Save', bodyTemplate: this.goalFormTpl })
      .subscribe(confirmed => {
        if (!confirmed || !this.editingGoalId) return;
        if (!this.goalName.trim() || !this.goalTarget || !this.goalTargetDate) return;
        this.repo
          .update(this.editingGoalId, { name: this.goalName.trim(), target: this.goalTarget, targetDate: this.goalTargetDate })
          .subscribe(() => this.refresh$.next());
      });
  }

  deleteGoal(goal: GoalItem): void {
    this.openMenuId.set(null);
    this.modalService
      .open({ title: `Delete "${goal.name}"?`, confirmLabel: 'Delete', cancelLabel: 'Cancel' })
      .subscribe(confirmed => {
        if (!confirmed) return;
        this.repo.delete(goal.id).subscribe(() => this.refresh$.next());
      });
  }

  openAddFundsModal(goal: GoalItem): void {
    this.contributingGoalId = goal.id;
    this.contributingGoalName = goal.name;
    this.contributeAmount = '';
    this.contributeNote = '';

    this.modalService
      .open({ title: `Add funds — ${goal.name}`, confirmLabel: 'Add funds', bodyTemplate: this.contributeFormTpl })
      .subscribe(confirmed => {
        if (!confirmed || !this.contributingGoalId) return;
        if (!this.contributeAmount || parseFloat(this.contributeAmount) <= 0) return;
        this.repo
          .contribute(this.contributingGoalId, { amount: this.contributeAmount, note: this.contributeNote.trim() || undefined })
          .subscribe(() => this.refresh$.next());
      });
  }
}
