import { Component, Input } from '@angular/core';
import { DashboardCardComponent } from '../../../shared/ui/card/dashboard-card.component';
import { SpendingSection } from '../../../shared/models/overview.models';

const SVG_W = 320, SVG_H = 110, PX = 4, PY = 8, LABEL_H = 18;

@Component({
  selector: 'app-spending-this-month',
  standalone: true,
  imports: [DashboardCardComponent],
  templateUrl: './spending-this-month.component.html',
  styleUrl: './spending-this-month.component.scss',
})
export class SpendingThisMonthComponent {
  @Input({ required: true }) data!: SpendingSection;

  readonly svgViewBox = `0 0 ${SVG_W} ${SVG_H}`;

  protected get chartBounds(): { min: number; max: number } {
    const all = this.data.points.flatMap(p => [parseFloat(p.thisMonth), parseFloat(p.lastMonth)]);
    return { min: Math.min(...all), max: Math.max(...all) };
  }

  protected polylinePoints(key: 'thisMonth' | 'lastMonth'): string {
    const { points } = this.data;
    const { min, max } = this.chartBounds;
    const range = max - min || 1;
    const plotH = SVG_H - PY * 2 - LABEL_H;
    return points
      .map((p, i) => {
        const x = PX + (i / (points.length - 1)) * (SVG_W - PX * 2);
        const y = PY + (1 - (parseFloat(p[key]) - min) / range) * plotH;
        return `${x.toFixed(1)},${y.toFixed(1)}`;
      })
      .join(' ');
  }

  protected get xLabels(): { x: number; label: string }[] {
    const { points } = this.data;
    const mid = Math.floor(points.length / 2);
    return [0, mid, points.length - 1].map(i => {
      const d = new Date(points[i].date + 'T00:00:00');
      const x = PX + (i / (points.length - 1)) * (SVG_W - PX * 2);
      return { x, label: `${d.toLocaleString('en-US', { month: 'short' })} ${d.getDate()}` };
    });
  }

  protected formatMoney(val: string): string {
    return `$${parseFloat(val).toFixed(2)}`;
  }
}
