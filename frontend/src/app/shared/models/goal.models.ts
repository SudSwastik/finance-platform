export interface GoalItem {
  id: string;
  name: string;
  current: string;
  target: string;
  percent: number;
  colorToken: string;
  targetDate: string;
}

export interface GoalContribution {
  id: string;
  goalId: string;
  amount: string;
  note: string | null;
  contributedAt: string;
}

export interface CreateGoalRequest {
  name: string;
  target: string;
  targetDate: string;
  colorToken?: string;
}

export interface UpdateGoalRequest {
  name?: string;
  target?: string;
  targetDate?: string;
}

export interface ContributeToGoalRequest {
  amount: string;
  note?: string;
}
