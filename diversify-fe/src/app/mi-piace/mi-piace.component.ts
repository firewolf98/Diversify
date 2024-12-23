import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importa CommonModule

@Component({
  selector: 'app-mi-piace',
  standalone: true,
  imports: [CommonModule], // Aggiungi CommonModule ai tuoi imports
  templateUrl: './mi-piace.component.html',
  styleUrls: ['./mi-piace.component.css']
})
export class MiPiaceComponent {
  items = [
    { id: 1, content: 'Contenuto del post 1', author: 'Autore 1', liked: false, likesCount: 0 },
    { id: 2, content: 'Contenuto del post 2', author: 'Autore 2', liked: false, likesCount: 0 },
    { id: 3, content: 'Contenuto del post 3', author: 'Autore 3', liked: false, likesCount: 0 }
  ];

  currentPage: number = 1;
  errorMessage: string = '';

  toggleLike(itemId: number): void {
    const item = this.items.find(i => i.id === itemId);
    if (item) {
      item.liked = !item.liked;
      item.likesCount += item.liked ? 1 : -1;
    } else {
      this.errorMessage = 'Errore nell\'azione Mi Piace';
    }
  }

  goToPrevious(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  goToNext(): void {
    this.currentPage++;
  }
}
