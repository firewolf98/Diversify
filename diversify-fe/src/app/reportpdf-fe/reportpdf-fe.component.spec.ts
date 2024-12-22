import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportpdfFEComponent } from './reportpdf-fe.component';

describe('ReportpdfFEComponent', () => {
  let component: ReportpdfFEComponent;
  let fixture: ComponentFixture<ReportpdfFEComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportpdfFEComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportpdfFEComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
