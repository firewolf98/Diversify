import { TestBed } from '@angular/core/testing';

import { DocumentoInformativoService } from './documento-informativo.service';

describe('DocumentoInformativoService', () => {
  let service: DocumentoInformativoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentoInformativoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
