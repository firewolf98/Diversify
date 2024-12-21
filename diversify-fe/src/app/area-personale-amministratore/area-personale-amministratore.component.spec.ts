import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AreaPersonaleAmministratoreComponent } from './area-personale-amministratore.component';

describe('AreaPersonaleAmministratoreComponent', () => {
  let component: AreaPersonaleAmministratoreComponent;
  let fixture: ComponentFixture<AreaPersonaleAmministratoreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AreaPersonaleAmministratoreComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AreaPersonaleAmministratoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
