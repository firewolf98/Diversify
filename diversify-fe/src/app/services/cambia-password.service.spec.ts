import { TestBed } from '@angular/core/testing';

import { CambiaPasswordService } from './cambia-password.service';

describe('CambiaPasswordService', () => {
  let service: CambiaPasswordService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CambiaPasswordService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
