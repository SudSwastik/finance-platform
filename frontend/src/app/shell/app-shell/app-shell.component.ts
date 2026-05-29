import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FixedNavigationComponent, NavItem } from '../fixed-navigation/fixed-navigation.component';
import { TopBarComponent } from '../top-bar/top-bar.component';
import { AppModalComponent } from '../../shared/ui/modal/app-modal.component';

const NAV_ITEMS: NavItem[] = [
  { label: 'Overview',        route: '/overview',       icon: '⊞' },
  { label: 'Wallets & Banks', route: '/wallets',        icon: '🏦' },
  { label: 'Activity Log',    route: '/activity',       icon: '📋' },
  { label: 'Money Movement',  route: '/movement',       icon: '↕' },
  { label: 'Insights',        route: '/insights',       icon: '💡' },
  { label: 'Spending Plan',   route: '/spending-plan',  icon: '📊' },
  { label: 'Subscriptions',   route: '/subscriptions',  icon: '🔄' },
  { label: 'Savings Goals',   route: '/goals',          icon: '🎯' },
  { label: 'Portfolio',       route: '/portfolio',      icon: '📈' },
  { label: 'Smart Tips',      route: '/tips',           icon: '✨' },
];

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [RouterOutlet, FixedNavigationComponent, TopBarComponent, AppModalComponent],
  templateUrl: './app-shell.component.html',
  styleUrl: './app-shell.component.scss',
})
export class AppShellComponent {
  protected readonly navItems = NAV_ITEMS;
}
