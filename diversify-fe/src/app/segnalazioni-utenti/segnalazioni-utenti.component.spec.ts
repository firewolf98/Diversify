import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SegnalazioniUtentiComponent } from './segnalazioni-utenti.component';

describe('SegnalazioniUtentiComponent', () => {
  let component: SegnalazioniUtentiComponent;
  let fixture: ComponentFixture<SegnalazioniUtentiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SegnalazioniUtentiComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SegnalazioniUtentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
