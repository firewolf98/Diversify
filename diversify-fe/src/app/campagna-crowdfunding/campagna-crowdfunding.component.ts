import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CampagnaService } from '../services/campagna-crowdfunding.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-campagna-crowdfunding',
  templateUrl: './campagna-crowdfunding.component.html',
  styleUrls: ['./campagna-crowdfunding.component.css'],
  imports: [CommonModule], // Aggiunto CommonModule
})
export class CampagnaCrowdfundingComponent implements OnInit {
  @Input() paese: string = ''; // Nome del Paese passato come input
  campagne: any[] = []; // Dati delle campagne
  campagnaSingola: any | null = null; // Dati di una singola campagna

  constructor(private campagnaService: CampagnaService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Ottenere il parametro 'paese' e 'idCampagna' dall'URL
    this.route.paramMap.subscribe((params) => {
      this.paese = params.get('paese') || ''; // Ottieni il parametro 'paese'
      const idCampagna = this.route.snapshot.queryParamMap.get('idCampagna'); // Ottieni il parametro 'idCampagna'

      if (idCampagna) {
        // Carica una campagna specifica
        this.loadCampagnaSingola(idCampagna);
      } else if (this.paese) {
        // Carica tutte le campagne per il paese
        this.loadCampagne(this.paese);
      } else {
        console.error("Parametro 'paese' non trovato nell'URL.");
      }
    });
  }

  private loadCampagne(paese: string): void {
    this.campagnaService.getCampagneByPaese(paese).subscribe(
      (data) => {
        this.campagne = data;
        console.log('Campagne caricate:', this.campagne);
      },
      (error) => {
        console.error('Errore nel caricamento delle campagne:', error);
      }
    );
  }

  private loadCampagnaSingola(idCampagna: string): void {
    this.campagnaService.getCampagnaById(idCampagna).subscribe(
      (data) => {
        this.campagnaSingola = data;
        console.log('Campagna singola caricata:', this.campagnaSingola);
      },
      (error) => {
        console.error('Errore nel caricamento della campagna singola:', error);
      }
    );
  }
}
