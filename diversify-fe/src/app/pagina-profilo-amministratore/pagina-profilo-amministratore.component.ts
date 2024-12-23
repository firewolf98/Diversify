import { Component, Input, Output, EventEmitter } from '@angular/core';
import { AreaPersonaleAmministratoreComponent } from '../area-personale-amministratore/area-personale-amministratore.component';
import { SegnalazionePaeseComponent } from '../segnalazione-paese/segnalazione-paese.component';
import { GestioneCampagneComponent } from '../gestione-campagne/gestione-campagne.component';
import { GestioneForumComponent } from '../gestione-forum/gestione-forum.component';
import { SegnalazioniUtentiComponent } from '../segnalazioni-utenti/segnalazioni-utenti.component';

@Component({
  selector: 'app-pagina-profilo-amministratore',
  templateUrl: './pagina-profilo-amministratore.component.html',
  styleUrls: ['./pagina-profilo-amministratore.component.css'],
})
export class PaginaProfiloAmministratoreComponent {
  @Output() setActiveComponent = new EventEmitter<string>();  // Emetti evento al componente padre
  activeLink: string | null = null;

  setActiveLink(event: MouseEvent): void {
    event.preventDefault();  // Previene il comportamento di default del link
    const clickedLink = (event.target as HTMLAnchorElement).textContent?.toLowerCase().replace(/\s/g, '');
    this.activeLink = clickedLink || null;
    if (clickedLink) {
      this.setActiveComponent.emit(clickedLink);  // Emetti l'evento con il componente selezionato
    }
  }
}
