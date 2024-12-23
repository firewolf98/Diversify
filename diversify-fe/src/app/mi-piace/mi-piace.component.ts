import { Component } from '@angular/core';

@Component({
  selector: 'app-mi-piace',
  templateUrl: './mi-piace.component.html',
  styleUrls: ['./mi-piace.component.css']
})
export class MiPiaceComponent {
  items = [
    { id: 1, content: 'Post 1', author: 'User1', liked: false, likesCount: 0 },
    { id: 2, content: 'Commento 1', author: 'User2', liked: false, likesCount: 2 },
    { id: 3, content: 'Subcommento 1', author: 'User3', liked: true, likesCount: 5 }
  ];
  
  currentPage: number = 1; // Inizializzazione della pagina corrente
  errorMessage: string = ''; // Inizializzazione della proprietà per gestire errori

  toggleLike(itemId: number): void {
    const item = this.items.find(i => i.id === itemId);

    if (item) {
      try {
        item.liked = !item.liked; // Inverti lo stato di "Mi Piace"
        item.likesCount += item.liked ? 1 : -1; // Aggiorna il conteggio

        // Resetta il messaggio di errore (se presente) dopo un'azione riuscita
        this.errorMessage = '';
      } catch (error) {
        // Imposta un messaggio di errore in caso di problemi
        this.errorMessage = 'Si è verificato un errore durante l\'aggiornamento dello stato di Mi Piace.';
      }
    } else {
      // Gestisce il caso in cui l'elemento non viene trovato
      this.errorMessage = 'Elemento non trovato.';
    }
  }

  goToNext(): void {
    this.currentPage++; // Incrementa la pagina corrente
  }

  goToPrevious(): void {
    if (this.currentPage > 1) {
      this.currentPage--; // Decrementa la pagina corrente
    }
  }
}
