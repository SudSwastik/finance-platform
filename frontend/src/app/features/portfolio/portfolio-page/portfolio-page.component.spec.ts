import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { PortfolioPageComponent } from './portfolio-page.component';
import { PortfolioRepository } from '../../../data-access/portfolio/portfolio.repository';
import { HoldingItem, Trade } from '../../../shared/models/portfolio.models';

class FakePortfolioRepository extends PortfolioRepository {
  override listHoldings() {
    return of<HoldingItem[]>([]);
  }

  override listTrades() {
    return of<Trade[]>([]);
  }
}

describe('PortfolioPageComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PortfolioPageComponent],
      providers: [{ provide: PortfolioRepository, useClass: FakePortfolioRepository }],
    }).compileComponents();
  });

  it('renders the .portfolio-page root class', () => {
    const fixture = TestBed.createComponent(PortfolioPageComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.portfolio-page')).toBeTruthy();
  });
});
