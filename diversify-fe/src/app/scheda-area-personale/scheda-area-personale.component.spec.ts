import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedaAreaPersonaleComponent } from './scheda-area-personale.component';

describe('SchedaAreaPersonaleComponent', () => {
  let component: SchedaAreaPersonaleComponent;
  let fixture: ComponentFixture<SchedaAreaPersonaleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchedaAreaPersonaleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchedaAreaPersonaleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
