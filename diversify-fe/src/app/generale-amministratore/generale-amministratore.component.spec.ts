import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GeneraleAmministratoreComponent } from './generale-amministratore.component';
import { PaginaProfiloAmministratoreComponent } from '../pagina-profilo-amministratore/pagina-profilo-amministratore.component';
import { AreaPersonaleAmministratoreComponent } from '../area-personale-amministratore/area-personale-amministratore.component';
import { SegnalazionePaeseComponent } from '../segnalazione-paese/segnalazione-paese.component';
import { GestioneCampagneComponent } from '../gestione-campagne/gestione-campagne.component';
import { GestioneForumComponent } from '../gestione-forum/gestione-forum.component';
import { SegnalazioniUtentiComponent } from '../segnalazioni-utenti/segnalazioni-utenti.component';
import { CommonModule } from '@angular/common';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('GeneraleAmministratoreComponent', () => {
  let component: GeneraleAmministratoreComponent;
  let fixture: ComponentFixture<GeneraleAmministratoreComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GeneraleAmministratoreComponent, PaginaProfiloAmministratoreComponent],
      imports: [
        AreaPersonaleAmministratoreComponent,
        SegnalazionePaeseComponent,
        GestioneCampagneComponent,
        GestioneForumComponent,
        SegnalazioniUtentiComponent,
        CommonModule
      ],
      schemas: [NO_ERRORS_SCHEMA]  // Per evitare errori se ci sono altri componenti non definiti
    });

    fixture = TestBed.createComponent(GeneraleAmministratoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should set the activeComponent when setActiveComponent is called', () => {
    const newComponent = 'segnalazionipaese';
    component.setActiveComponent(newComponent);
    expect(component.activeComponent).toBe(newComponent);
  });

  it('should call setActiveComponent when a link is clicked in PaginaProfiloAmministratoreComponent', () => {
    // Simula il passaggio di setActiveComponent al componente figlio
    const mockSetActiveComponent = spyOn(component, 'setActiveComponent');

    // Crea un'istanza del componente figlio
    const paginaProfiloAmministratoreComponent = fixture.debugElement.children[0].componentInstance;
    
    // Simula un evento di click su un link nel componente figlio
    const event = new MouseEvent('click', { bubbles: true, cancelable: true });
    const link = { textContent: 'Segnalazione Paese' };  // Simula il testo del link
    paginaProfiloAmministratoreComponent.setActiveLink(event);  // Chiamata al metodo setActiveLink

    fixture.detectChanges();

    // Verifica che la funzione setActiveComponent sia stata chiamata
    expect(mockSetActiveComponent).toHaveBeenCalledWith('segnalazionipaese');
  });
});
