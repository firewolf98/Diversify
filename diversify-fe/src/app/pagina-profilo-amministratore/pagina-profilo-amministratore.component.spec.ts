import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PaginaProfiloAmministratoreComponent } from './pagina-profilo-amministratore.component';

describe('PaginaProfiloAmministratoreComponent', () => {
  let component: PaginaProfiloAmministratoreComponent;
  let fixture: ComponentFixture<PaginaProfiloAmministratoreComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaProfiloAmministratoreComponent]
    });
    fixture = TestBed.createComponent(PaginaProfiloAmministratoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should set activeLink when setActiveLink is called', () => {
    component.setActiveLink({
      target: { textContent: 'Area Personale' }
    } as any);  // Simula un MouseEvent con un testo di link
    expect(component.activeLink).toBe('areapersonale');
  });

  it('should update activeLink on link click', () => {
    const compiled = fixture.nativeElement;
    const link = compiled.querySelector('a');  // Seleziona il primo link nel template

    // Crea un MouseEvent per simulare il click
    const event = new MouseEvent('click', { bubbles: true, cancelable: true });
    link.dispatchEvent(event);

    fixture.detectChanges();

    // Verifica che activeLink sia stato aggiornato
    expect(component.activeLink).toBe('areapersonale');  // Assicurati che il testo del link corrisponda al valore
  });

  it('should call setActiveComponent when a link is clicked', () => {
    const mockSetActiveComponent = jasmine.createSpy('setActiveComponent');
    component.setActiveComponent = mockSetActiveComponent;

    const compiled = fixture.nativeElement;
    const link = compiled.querySelector('a');

    const event = new MouseEvent('click', { bubbles: true, cancelable: true });
    link.dispatchEvent(event);

    fixture.detectChanges();

    // Verifica che la funzione setActiveComponent sia stata chiamata con il valore giusto
    expect(mockSetActiveComponent).toHaveBeenCalledWith('areapersonale');  // Assicurati che il valore corrisponda
  });
});