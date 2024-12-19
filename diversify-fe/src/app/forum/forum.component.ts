import { Component } from '@angular/core';

@Component({
  selector: 'app-forum',
  standalone: true,
  templateUrl: './forum.component.html',
  styleUrls: ['./forum.component.css']
})
export class ForumComponent {
  currentPage: number = 1;  // Inizializzazione della pagina corrente

  // Metodo per andare alla pagina successiva
  goToNext(): void {
    this.currentPage++;  // Incrementa la pagina corrente
  }

  // Metodo per tornare alla pagina precedente
  goToPrevious(): void {
    if (this.currentPage > 1) {
      this.currentPage--;  // Decrementa la pagina corrente
    }
  }
}
