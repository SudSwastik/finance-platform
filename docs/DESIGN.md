# Design

Source mockup: `dashboard.webp`. Use a personal app name — do not ship third-party branding.

---

## Layout

| Region | Description |
|--------|-------------|
| **Sidebar** (fixed left) | Logo, nav items, footer links, user profile |
| **Top bar** | Global search, Customize action |
| **Main** | Responsive card grid (2 columns on desktop) |

### Nav items

| Label | Route |
|-------|-------|
| Overview | `/overview` |
| Wallets & Banks | `/wallets` |
| Subscriptions | `/subscriptions` |
| Portfolio | `/portfolio` |

Footer: Settings. Bottom: user avatar + display name.

---

## Overview page — current widgets

### Total Budgets
- Header: title, total amount, dropdown filter
- Allocation bar: horizontal multi-segment (4 category colors)
- Legend rows: name, spent, % of budget, budget cap

| Category | Color token | Sample spent | Sample budget |
|----------|-------------|--------------|---------------|
| Essentials | `category.essentials` | $1,750 | $2,800 |
| Lifestyles | `category.lifestyles` | $900 | $2,000 |
| Occasional | `category.occasional` | $1,170 | $1,600 |
| Others | `category.others` | $1,300 | $2,000 |

### Investments
- Header: title, "See all" link
- Rows: symbol/name, cost basis, % change (green +, red -), market value

| Symbol | Cost basis | Change | Value |
|--------|------------|--------|-------|
| AAPL | $1,600 | +21.9% | $1,950 |
| TSLA | $2,000 | +15% | $2,300 |
| BTC | $1,100 | -19.1% | $890 |

### Recurring
- Header: title, "See all" link
- Rows: name, frequency, amount, next payment date

| Name | Frequency | Amount | Next |
|------|-----------|--------|------|
| Spotify Premium | Monthly | $10.99 | 15 Jul |
| ChatGPT Plus | Monthly | $20.00 | 18 Jul |
| YouTube Premium | Monthly | $11.99 | 22 Jul |

---

## Design tokens

Full machine-readable values: `docs/design-tokens.json`.

### Colors

| Token | Hex | Usage |
|-------|-----|-------|
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
- Font: system UI stack or Inter
- KPI amounts: semibold, 24–32px
- Card titles: 16–18px semibold
- Body/list: 14px regular
- Meta/dates: 12–13px, `text.secondary`

### Spacing + shape
- Card radius: 12–16px
- Card padding: 20–24px
- Grid gap: 16–24px
- Sidebar width: ~240–280px
- Category pill: full rounded (border-radius: 9999px)

---

## Component inventory

| Component | Location |
|-----------|----------|
| `AppShellComponent` | `shell/app-shell/` |
| `FixedNavigationComponent` | `shell/fixed-navigation/` |
| `TopBarComponent` | `shell/top-bar/` |
| `DashboardCardComponent` | `shared/ui/card/` |
| `AppModalComponent` | `shared/ui/modal/` |
| `AppButtonComponent` | `shared/ui/button/` |
| `TotalBudgetsComponent` | `features/overview/total-budgets/` |
| `InvestmentsWidgetComponent` | `features/overview/investments-widget/` |
| `RecurringWidgetComponent` | `features/overview/recurring-widget/` |

---

## Accessibility

- Sidebar: `nav` landmark; `aria-current="page"` on active item
- Color: never rely on color alone for positive/negative — include sign or label
- Focus visible on all interactive elements
