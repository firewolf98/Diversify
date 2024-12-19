import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-popup-grid',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './popup-grid.component.html',
  styleUrls: ['./popup-grid.component.css']
})
export class PopupGridComponent {
  constructor(private router: Router) {}

  countryName: string = 'Malta';
  flag: string = '🇲🇹';

  buttons = [
    { label: "Leggi di Malta sulla sicurezza e l'inclusività", color: "pink", size: "large" },
    { label: "Forum: LGBTQ+", color: "green", size: "large" },
    { label: "Forum: Turisti a Malta", color: "pink", size: "small" },
    { label: "Tutti i forum maltesi", color: "blue", size: "small" },
    { label: "Campagna di crowdfunding: Ċentru għall-persuni b'diżabilità f'Marsaxlokk", color: "green", size: "medium" },
    { label: "Tutte le campagne di crowdfunding", color: "blue", size: "medium" }
  ];

  goBack() {
    // Naviga indietro di una pagina
    this.router.navigate(['/']); // Puoi sostituire con il percorso della mappa
  }
}
