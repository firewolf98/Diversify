import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AperturaChatbotComponent } from './apertura-chatbot.component';

describe('AperturaChatbotComponent', () => {
  let component: AperturaChatbotComponent;
  let fixture: ComponentFixture<AperturaChatbotComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AperturaChatbotComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AperturaChatbotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
