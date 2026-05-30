import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FixedNavigationComponent, NavItem } from '../fixed-navigation/fixed-navigation.component';
import { TopBarComponent } from '../top-bar/top-bar.component';
import { AppModalComponent } from '../../shared/ui/modal/app-modal.component';

const NAV_ITEMS: NavItem[] = [
  { label: 'Overview',        route: '/overview',       icon: '⊞' },
  { label: 'Wallets & Banks', route: '/wallets',        icon: '🏦' },
  { label: 'Subscriptions',   route: '/subscriptions',  icon: '🔄' },
  { label: 'Portfolio',       route: '/portfolio',      icon: '📈' },
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
