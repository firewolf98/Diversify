import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SegnalazionePaeseComponent } from './segnalazione-paese.component';

describe('SegnalazionePaeseComponent', () => {
  let component: SegnalazionePaeseComponent;
  let fixture: ComponentFixture<SegnalazionePaeseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SegnalazionePaeseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SegnalazionePaeseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
