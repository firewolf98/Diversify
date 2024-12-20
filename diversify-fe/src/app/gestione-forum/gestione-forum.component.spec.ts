import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestioneForumComponent } from './gestione-forum.component';

describe('GestioneForumComponent', () => {
  let component: GestioneForumComponent;
  let fixture: ComponentFixture<GestioneForumComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestioneForumComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestioneForumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
