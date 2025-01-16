import { TestBed } from '@angular/core/testing';

import { ClosePopupService } from './close-popup.service';

describe('ClosePopupService', () => {
  let service: ClosePopupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClosePopupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
