import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentoInformativoComponent } from './documento-informativo.component';

describe('DocumentoInformativoComponent', () => {
  let component: DocumentoInformativoComponent;
  let fixture: ComponentFixture<DocumentoInformativoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentoInformativoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DocumentoInformativoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
