import { TestBed } from '@angular/core/testing';
import { MiPiaceService } from './mi-piace.service';

describe('MiPiaceService', () => {
  let service: MiPiaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MiPiaceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
