import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { data } from 'cypress/types/jquery';

@Component({
  selector: 'app-segnalazioni-utenti',
  templateUrl: './segnalazioni-utenti.component.html',
  styleUrls: ['./segnalazioni-utenti.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class SegnalazioniUtentiComponent {
  segnalazioni = [
    {
      segnalato: 'utenteSegnalato1',
      segnalatore: 'utenteSegnalatore1',
      motivo: 'Lingaggio offensivo',
      tipo: 'Post',
      data: '2021-06-01'
    },
    {
      segnalato: 'utenteSegnalato2',
      segnalatore: 'utenteSegnalatore2',
      motivo: 'Contenuto inappropriato',
      tipo: 'Commento',
      data: '2021-06-01'
    },
    {
      segnalato: 'ciro',
      segnalatore: 'gennaro',
      motivo: 'è napoletano ma tifa juve',
      tipo: 'post',
      data: '2021-06-01'
    },
    {
      segnalato: 'mariarca',
      segnalatore: 'mariaines',
      motivo: 'non ha sceso il panaro quando gliel\'ho chiesto',
      tipo: 'commento',
      data: '2021-06-01'
    },
  ];

  // Variabili per gestire il popup di ban
  showBanPopup = false;
  selectedReport: any = null;

  // Funzione per aprire il popup di ban
  openBanPopup(report: any) {
    this.selectedReport = report;
    this.showBanPopup = true;
  }

  // Funzione per chiudere il popup di ban
  closeBanPopup() {
    this.showBanPopup = false;
    this.selectedReport = null;
  }

  // Funzione per bannare l'utente
  banUser() {
    const index = this.segnalazioni.indexOf(this.selectedReport);
    if (index > -1) {
      // Rimuoviamo la segnalazione dalla lista
      this.segnalazioni.splice(index, 1);
    }
    this.closeBanPopup();  // Chiudiamo il popup dopo aver bannato
  }
}
