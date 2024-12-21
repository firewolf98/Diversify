import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestioneCampagneComponent } from './gestione-campagne.component';

describe('GestioneCampagneComponent', () => {
  let component: GestioneCampagneComponent;
  let fixture: ComponentFixture<GestioneCampagneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestioneCampagneComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestioneCampagneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
