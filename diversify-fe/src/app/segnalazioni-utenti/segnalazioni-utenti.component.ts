import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ForumService } from '../services/forum.service';


@Component({
  selector: 'app-segnalazioni-utenti',
  templateUrl: './segnalazioni-utenti.component.html',
  styleUrls: ['./segnalazioni-utenti.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class SegnalazioniUtentiComponent {
  segnalazioni: any;

  constructor(private forumService: ForumService) {

  }

  ngOnInit() {
    this.fetchSegnalazioni();
  }

  fetchSegnalazioni() {
    this.forumService.getAllReport().subscribe({
      next: (data) => {
        this.segnalazioni = data;
      },
      error: (err) => {
        console.error("Errore:", err);
      }
    });
  }

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
  banUser(report: any) {
    this.forumService.banUser(report.idSegnalazione).subscribe({
      next: (data) => {
        alert("Utente bannato");
        this.fetchSegnalazioni();
      },
      error: (err) => {
        console.error("Errore:", err);
      }
    });
    this.closeBanPopup();  // Chiudiamo il popup dopo aver bannato
  }
}