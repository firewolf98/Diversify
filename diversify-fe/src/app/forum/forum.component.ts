import { Component } from '@angular/core';
import { Router } from '@angular/router'; // Importa il Router
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-forum',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './forum.component.html',
  styleUrls: ['./forum.component.css']
})
export class ForumComponent {
  currentPage: number = 1;  // Inizializzazione della pagina corrente

  constructor(private router: Router) {} // Aggiungi il costruttore con il Router

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

  // Metodo per creare un nuovo post
  createNewPost(): void {
    console.log('Creazione di un nuovo post...');
    this.router.navigate(['/post']); // Naviga alla rotta 'post'
  }
}
