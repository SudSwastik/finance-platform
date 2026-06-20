import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent, NavItem } from '../sidebar/sidebar.component';
import { PageHeaderComponent } from '../page-header/page-header.component';
import { AppModalComponent } from '../../shared/ui/modal/app-modal.component';

const NAV_ITEMS: NavItem[] = [
  { label: 'Overview',        route: '/overview',       icon: '⊞' },
  { label: 'Wallets & Banks', route: '/wallets',        icon: '🏦' },
  { label: 'Subscriptions',   route: '/subscriptions',  icon: '🔄' },
  { label: 'Portfolio',       route: '/portfolio',      icon: '📈' },
];

@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, PageHeaderComponent, AppModalComponent],
  templateUrl: './dashboard-layout.component.html',
  styleUrl: './dashboard-layout.component.scss',
})
export class DashboardLayoutComponent {
  protected readonly navItems = NAV_ITEMS;
}
