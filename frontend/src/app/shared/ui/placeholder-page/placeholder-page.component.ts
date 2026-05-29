import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-placeholder-page',
  standalone: true,
  template: `
    <div class="placeholder-page">
      <p class="text-secondary">{{ title }} — coming soon</p>
    </div>
  `,
  styles: [`
    .placeholder-page {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100%;
      font-size: var(--font-size-card-title);
    }
  `],
})
export class PlaceholderPageComponent {
  protected readonly title = inject(ActivatedRoute).snapshot.data['title'] ?? 'This page';
}
