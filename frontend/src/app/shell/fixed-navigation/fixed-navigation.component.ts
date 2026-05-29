import { Component, Input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

export interface NavItem {
  label: string;
  route: string;
  icon?: string;
}

@Component({
  selector: 'app-fixed-navigation',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './fixed-navigation.component.html',
  styleUrl: './fixed-navigation.component.scss',
})
export class FixedNavigationComponent {
  @Input() navItems: NavItem[] = [];
  @Input() userName = '';
  @Input() userInitials = '';
}
