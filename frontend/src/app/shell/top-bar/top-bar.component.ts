import { Component } from '@angular/core';
import { AppButtonComponent } from '../../shared/ui/button/app-button.component';

@Component({
  selector: 'app-top-bar',
  standalone: true,
  imports: [AppButtonComponent],
  templateUrl: './top-bar.component.html',
  styleUrl: './top-bar.component.scss',
})
export class TopBarComponent {}
