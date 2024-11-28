import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrazioneFormComponent } from './registrazione-form.component';

describe('RegistrazioneFormComponent', () => {
  let component: RegistrazioneFormComponent;
  let fixture: ComponentFixture<RegistrazioneFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrazioneFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrazioneFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
