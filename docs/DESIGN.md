# Design specification

Source mockup: [dashboard.webp](../dashboard.webp) (Fundwise-style personal finance UI).

**Product naming:** Use a personal app name in implementation — do not ship third-party branding ("Fundwise", sample user "Choirul Syafril") unless intentionally chosen.

---

## Layout

### Shell

| Region | Description |
|--------|-------------|
| **Sidebar** (fixed left) | Logo, search, primary nav, promo card, footer links, user profile |
| **Top bar** | Global search, **Customize** action |
| **Main** | Responsive card grid (2 columns on desktop for paired widgets) |

### Sidebar navigation

| Item | Route (proposed) | MVP |
|------|------------------|-----|
| Overview | `/overview` | Yes |
| Wallets & Banks | `/wallets` | Placeholder |
| Activity Log | `/activity` | Placeholder |
| Money Movement | `/movement` | Placeholder |
| Insights | `/insights` | Placeholder |
| Spending Plan | `/spending-plan` | Placeholder |
| Subscriptions | `/subscriptions` | Placeholder |
| Savings Goals | `/goals` | Widget on Overview; full page later |
| Portfolio | `/portfolio` | Placeholder |
| Smart Tips | `/tips` | Placeholder |

Footer: Help & Support, Settings. Bottom: user avatar + display name.

### Promo widget (sidebar)

- Discount / trial messaging, progress bar, **Upgrade** CTA — optional for personal use; can hide via feature flag.

---

## Overview page widgets

### Row 1

#### Total budgets (left, wide)

- **Header:** title, total amount (`$6,400`), dropdown filter (e.g. "Expenses")
- **Allocation bar:** horizontal multi-segment bar (4 category colors)
- **Legend rows:** icon, name, spent, % of budget, budget cap

| Category | Color token | Sample spent | Sample budget | Sample % |
|----------|-------------|--------------|---------------|----------|
| Essentials | `category.essentials` | $1,750 | $2,800 | 62.5% |
| Lifestyles | `category.lifestyles` | $900 | $2,000 | 45.0% |
| Occasional | `category.occasional` | $1,170 | $1,600 | 73.1% |
| Others | `category.others` | $1,300 | $2,000 | 65.0% |

#### Spending this month (right)

- **Header:** title, current total (`$350.00`), comparison dropdown ("This month vs. last month")
- **Chart:** dual line — solid (this month), dashed (last month)
- **X-axis:** date range (e.g. Jul 6–Jul 12)
- **Highlight:** point on selected date with value tooltip

### Row 2

#### Goals (left)

- **Header:** "Goals", **See all** link
- **Rows:** icon, name, current/target, progress bar + trophy, %, target date

| Goal | Current | Target | % | Color | Target date |
|------|---------|--------|---|-------|-------------|
| Vacation | $2,300 | $3,000 | 76.7% | purple | Nov 2025 |
| House | $5,800 | $10,000 | 58% | green | Jul 2026 |
| Car | $1,450 | $5,000 | 29% | orange | Mar 2026 |

#### Transactions (right)

- **Header:** "Transactions", **See all** link
- **Rows:** icon, merchant, category pill, amount (negative, red), date

| Merchant | Category pill | Amount | Date |
|----------|---------------|--------|------|
| Groceries | Essential | -$128.45 | 11 Jul |
| Gift | Occasional | -$240.00 | 10 Jul |
| Coffee | Lifestyle | -$200.00 | 8 Jul |

### Row 3

#### Investments (left)

- **Header:** "Investments", **See all** link
- **Rows:** logo, ticker/name, basis or cost, % change (green + / red -), market value

| Symbol | Cost basis | Change | Value |
|--------|------------|--------|-------|
| AAPL | $1,600.00 | +21.9% | $1,950.00 |
| TSLA | $2,000.00 | +15% | $2,300.00 |
| BTC | $1,100.00 | -19.1% | $890.00 |

#### Recurring (right)

- **Header:** "Recurring", **See all** link
- **Rows:** logo, name, frequency, amount, next payment date

| Name | Frequency | Amount | Next |
|------|-----------|--------|------|
| Spotify Premium | Monthly | $10.99 | 15 Jul |
| ChatGPT Plus | Monthly | $20.00 | 18 Jul |
| YouTube Premium | Monthly | $11.99 | 22 Jul |

---

## Design tokens

See [design-tokens.json](design-tokens.json) for machine-readable values.

### Colors

| Token | Hex (approx.) | Usage |
|-------|---------------|--------|
| `brand.primary` | `#7C3AED` | Logo, active nav, primary accents |
| `category.essentials` | `#8B5CF6` | Essentials budget segment |
| `category.lifestyles` | `#22C55E` | Lifestyles |
| `category.occasional` | `#F97316` | Occasional |
| `category.others` | `#EAB308` | Others |
| `semantic.positive` | `#16A34A` | Gains, on-track |
| `semantic.negative` | `#DC2626` | Spend, losses |
| `surface.page` | `#F3F4F6` | Page background |
| `surface.card` | `#FFFFFF` | Card background |
| `text.primary` | `#111827` | Headings, amounts |
| `text.secondary` | `#6B7280` | Labels, meta |
| `border.subtle` | `#E5E7EB` | Card borders |

### Typography

- **Font family:** System UI stack or Inter / similar sans-serif
- **Page title / KPI:** semibold–bold, large (e.g. 24–32px for totals)
- **Card title:** 16–18px semibold
- **Body / list:** 14px regular
- **Meta / dates:** 12–13px, `text.secondary`

### Spacing & shape

- **Card radius:** 12–16px
- **Card padding:** 20–24px
- **Grid gap:** 16–24px
- **Sidebar width:** ~240–280px
- **Category pill:** full rounded (9999px), small horizontal padding

### Elevation

- Cards: very subtle shadow or 1px border — avoid heavy drop shadows

---

## Component inventory

Implementation mapping: [FRONTEND_ARCHITECTURE.md](FRONTEND_ARCHITECTURE.md) (scoped folders, shell split).

| Component | Location | Used in |
|-----------|----------|---------|
| `AppShellComponent` | `shell/app-shell/` | Layout wrapper |
| `FixedNavigationComponent` | `shell/fixed-navigation/` | Nav + promo + profile |
| `TopBarComponent` | `shell/top-bar/` | Search + Customize |
| `DashboardCardComponent` | `shared/ui/card/` | All overview widgets |
| `TotalBudgetsComponent` | `features/overview/total-budgets/` | Total budgets (scoped `.total-budgets`) |
| `SpendingThisMonthComponent` | `features/overview/spending-this-month/` | Spending chart |
| `GoalsWidgetComponent` | `features/overview/goals-widget/` | Goals list |
| `TransactionsWidgetComponent` | `features/overview/transactions-widget/` | Transactions |
| `InvestmentsWidgetComponent` | `features/overview/investments-widget/` | Holdings |
| `RecurringWidgetComponent` | `features/overview/recurring-widget/` | Subscriptions |
| `AppModalComponent` | `shared/ui/modal/` | All dialogs (only modal entry) |
| `AppButtonComponent` | `shared/ui/button/` | Actions + modal footers |

| Component (legacy name) | Used in |
|-----------|---------|
| `AppShell` | → `AppShellComponent` |
| `Sidebar` | → `FixedNavigationComponent` |
| `TopBar` | → `TopBarComponent` |
| `DashboardCard` | → `DashboardCardComponent` |
| `BudgetAllocationBar` | Total budgets |
| `CategoryBudgetRow` | Budget legend |
| `SpendingLineChart` | Spending this month |
| `GoalProgressRow` | Goals list |
| `TransactionRow` | Transactions list |
| `HoldingRow` | Investments list |
| `RecurringRow` | Recurring list |
| `CategoryPill` | Transaction tags |
| `PercentChange` | Investments (+/- coloring) |

---

## Sample TypeScript shapes (Angular models)

```typescript
type Money = string; // ISO decimal string e.g. "128.45"

interface BudgetCategory {
  id: string;
  name: string;
  colorToken: string;
  spent: Money;
  budget: Money;
  percentUsed: number;
}

interface SpendingSeriesPoint {
  date: string; // ISO date
  thisMonth: Money;
  lastMonth: Money;
}

interface Goal {
  id: string;
  name: string;
  icon: string;
  current: Money;
  target: Money;
  percent: number;
  colorToken: string;
  targetDate: string;
}

interface Transaction {
  id: string;
  merchant: string;
  category: 'essential' | 'lifestyle' | 'occasional' | 'other';
  amount: Money; // negative for outflow
  date: string;
}

interface Holding {
  id: string;
  symbol: string;
  name: string;
  costBasis: Money;
  changePercent: number;
  value: Money;
}

interface RecurringBill {
  id: string;
  name: string;
  frequency: 'monthly' | 'yearly' | 'weekly';
  amount: Money;
  nextDate: string;
}
```

---

## Accessibility

- Sidebar: `nav` landmark, `aria-current="page"` on active item
- Charts: text summary or data table fallback for screen readers
- Color: do not rely on color alone for positive/negative — include sign or label
- Focus visible on all interactive elements
