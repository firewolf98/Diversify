import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CampagnaCrowdfundingComponent } from './campagna-crowdfunding.component';

describe('CampagnaCrowdfundingComponent', () => {
  let component: CampagnaCrowdfundingComponent;
  let fixture: ComponentFixture<CampagnaCrowdfundingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CampagnaCrowdfundingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CampagnaCrowdfundingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
