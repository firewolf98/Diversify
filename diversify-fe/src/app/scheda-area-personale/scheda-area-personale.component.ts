import { Component } from '@angular/core';

@Component({
  selector: 'app-scheda-area-personale',
  templateUrl: './scheda-area-personale.component.html',
  styleUrls: ['./scheda-area-personale.component.css']
})
export class SchedaAreaPersonaleComponent {
  username = 'utente123';
  nome = 'Giovanni';
  cognome = 'Rossi';
  email = 'giovanni.rossi@example.com';
  domandaPersonale = 'Qual è il tuo colore preferito?';


  // Funzione per modificare la password
  modificaPassword() {
    console.log('Modifica password');
  }

  // Funzione per eliminare l'account
  eliminaAccount() {
    console.log('Elimina account');
  }
}
