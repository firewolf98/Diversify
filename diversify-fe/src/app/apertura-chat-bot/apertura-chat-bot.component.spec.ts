import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AperturaChatBotComponent } from './apertura-chat-bot.component';

describe('AperturaChatBotComponent', () => {
  let component: AperturaChatBotComponent;
  let fixture: ComponentFixture<AperturaChatBotComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AperturaChatBotComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AperturaChatBotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
