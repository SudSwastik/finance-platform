# Design

Source: `design/Overview.dc.html`. Dark-mode, data-first dashboard — Ledgerly.

Full design tokens (colors, fonts, icons, components, radii): see `CLAUDE.md`.
Machine-readable tokens: `docs/design-tokens.json`.

---

## Layout

- **Shell:** fixed sidebar (248px) + flex `<main>`
- **Sidebar:** logo → account switcher → MAIN group → OTHER group → user card (`margin-top:auto`)
- **Main:** `padding:26px 32px 40px`, 12-column grid (`gap:18px`)

### Nav

| Group | Items |
|-------|-------|
| MAIN | Overview, Wallets & Banks, Portfolio, Transactions, Subscriptions, Goals |
| OTHER | Integrations, Settings, Get Help |

---

## Overview page — widgets

| Widget | Span | Description |
|--------|------|-------------|
| Net worth | 8 cols | Hero `$248,310`, 12M line chart, segmented 1M/3M/12M control |
| Allocation | 4 cols | Horizontal bar (Investments 57%, Crypto 22%, Cash 21%) + legend |
| This month | 4 cols | Income + spending progress bars, net saved |
| Goals | 4 cols | 3 goals with progress bars, "View all" link |
| Upcoming | 4 cols | Next 4 recurring payments |
| Recent transactions | 8 cols | Last 5 transactions (icon tile, category, date, amount) |
| Top holdings | 4 cols | 4 holdings (ticker, shares, value, % change) |

---

## Component inventory

| Component | Path |
|-----------|------|
| `DashboardLayoutComponent` | `layout/dashboard-layout/` |
| `SidebarComponent` | `layout/sidebar/` |
| `PageHeaderComponent` | `layout/page-header/` |
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
