import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionePasswordFormComponent } from './gestione-password-form.component';

describe('GestionePasswordFormComponent', () => {
  let component: GestionePasswordFormComponent;
  let fixture: ComponentFixture<GestionePasswordFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionePasswordFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionePasswordFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
