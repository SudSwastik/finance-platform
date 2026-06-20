import { Component } from '@angular/core';
import { AppButtonComponent } from '../../shared/ui/button/app-button.component';

@Component({
  selector: 'app-page-header',
  standalone: true,
  imports: [AppButtonComponent],
  templateUrl: './page-header.component.html',
  styleUrl: './page-header.component.scss',
})
export class PageHeaderComponent {}
