import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiPiaceComponent } from './mi-piace.component';

describe('MiPiaceComponent', () => {
  let component: MiPiaceComponent;
  let fixture: ComponentFixture<MiPiaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MiPiaceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MiPiaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
