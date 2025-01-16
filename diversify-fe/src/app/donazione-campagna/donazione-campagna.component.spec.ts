import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DonazioneCampagnaComponent } from './donazione-campagna.component';

describe('DonazioneCampagnaComponent', () => {
  let component: DonazioneCampagnaComponent;
  let fixture: ComponentFixture<DonazioneCampagnaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DonazioneCampagnaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DonazioneCampagnaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
