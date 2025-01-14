import { TestBed } from '@angular/core/testing';

import { SearchingCountryService } from './searching-country.service';

describe('SearchingCountryService', () => {
  let service: SearchingCountryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchingCountryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
