import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { GoalsPageComponent } from './goals-page.component';
import { GoalRepository } from '../../../data-access/goals/goal.repository';
import { GoalContribution, GoalItem } from '../../../shared/models/goal.models';

class FakeGoalRepository extends GoalRepository {
  override list() {
    return of<GoalItem[]>([]);
  }

  override create() {
    return of({} as GoalItem);
  }

  override update() {
    return of({} as GoalItem);
  }

  override delete() {
    return of(void 0);
  }

  override listContributions() {
    return of<GoalContribution[]>([]);
  }

  override contribute() {
    return of({} as GoalItem);
  }
}

describe('GoalsPageComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GoalsPageComponent],
      providers: [{ provide: GoalRepository, useClass: FakeGoalRepository }],
    }).compileComponents();
  });

  it('renders the .goals-page root class', () => {
    const fixture = TestBed.createComponent(GoalsPageComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.goals-page')).toBeTruthy();
  });
});
