import { Component } from '@angular/core';  
import { PaginaProfiloAmministratoreComponent } from '../pagina-profilo-amministratore/pagina-profilo-amministratore.component';
import { AreaPersonaleAmministratoreComponent } from '../area-personale-amministratore/area-personale-amministratore.component';
import { SegnalazionePaeseComponent } from '../segnalazione-paese/segnalazione-paese.component';
import { GestioneCampagneComponent } from '../gestione-campagne/gestione-campagne.component';
import { GestioneForumComponent } from '../gestione-forum/gestione-forum.component';
import { SegnalazioniUtentiComponent } from '../segnalazioni-utenti/segnalazioni-utenti.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-generale-amministratore',
  standalone: true,
  templateUrl: './generale-amministratore.component.html',
  styleUrls: ['./generale-amministratore.component.css'],
  imports: [
    PaginaProfiloAmministratoreComponent,
    AreaPersonaleAmministratoreComponent,
    SegnalazionePaeseComponent,
    GestioneCampagneComponent,
    GestioneForumComponent,
    SegnalazioniUtentiComponent,
    CommonModule
  ]
})
export class GeneraleAmministratoreComponent {
  activeComponent: string = 'areapersonale';  // Inizialmente visualizza 'Area Personale'

  // Funzione per cambiare il componente a sinistra in base al link cliccato
  setActiveComponent(component: string) {
    console.log('Componente selezionato:', component);  // Log per vedere quale componente viene selezionato
    this.activeComponent = component;
  }
}
