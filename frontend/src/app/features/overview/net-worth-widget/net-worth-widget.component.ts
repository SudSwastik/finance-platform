import { Component, Input, signal } from '@angular/core';
import { NetWorthData } from '../../../shared/models/overview.models';

type Period = '1M' | '3M' | '12M';

@Component({
  selector: 'app-net-worth-widget',
  standalone: true,
  imports: [],
  templateUrl: './net-worth-widget.component.html',
  styleUrl: './net-worth-widget.component.scss',
})
export class NetWorthWidgetComponent {
  @Input({ required: true }) data!: NetWorthData;

  protected readonly periods: Period[] = ['1M', '3M', '12M'];
  protected readonly selectedPeriod = signal<Period>('12M');

  protected selectPeriod(p: Period): void {
    this.selectedPeriod.set(p);
  }

  protected fmtTotal(): string {
    const n = parseFloat(this.data.total);
    const [whole, dec] = n.toLocaleString('en-US', { minimumFractionDigits: 2 }).split('.');
    return `$${whole}__.__${dec}`;
  }

  protected get whole(): string {
    const n = parseFloat(this.data.total);
    return '$' + Math.floor(n).toLocaleString('en-US');
  }

  protected get cents(): string {
    const dec = parseFloat(this.data.total).toFixed(2).split('.')[1];
    return '.' + dec;
  }

  protected get vsLastMonthFmt(): string {
    const n = parseFloat(this.data.vsLastMonth);
    return `+$${n.toLocaleString('en-US', { minimumFractionDigits: 0 })} vs last month`;
  }

  protected readonly MONTHS = ['Jul','Aug','Sep','Oct','Nov','Dec','Jan','Feb','Mar','Apr','May','Jun'];

  protected polylinePoints(): string {
    const W = 640, n = this.data.chartY.length;
    return this.data.chartY
      .map((y, i) => `${Math.round(i * W / (n - 1))},${y}`)
      .join(' ');
  }

  protected fillPath(): string {
    const pts = this.polylinePoints().split(' ');
    const last = pts[pts.length - 1].split(',');
    return `M${pts[0]} ${pts.slice(1).map(p => 'L' + p).join(' ')} L${last[0]},180 L0,180 Z`;
  }

  protected lastPoint(): { cx: number; cy: number } {
    const W = 640, y = this.data.chartY;
    return { cx: W, cy: y[y.length - 1] };
  }
}
