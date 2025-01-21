import { TestBed } from '@angular/core/testing';

import { CampagnaCrowdfundingService } from './campagna-crowdfunding.service';

describe('CampagnaCrowdfundingService', () => {
  let service: CampagnaCrowdfundingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CampagnaCrowdfundingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
