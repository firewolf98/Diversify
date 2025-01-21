import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IconaBrigidComponent } from './icona-brigid.component';

describe('IconaBrigidComponent', () => {
  let component: IconaBrigidComponent;
  let fixture: ComponentFixture<IconaBrigidComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IconaBrigidComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IconaBrigidComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
