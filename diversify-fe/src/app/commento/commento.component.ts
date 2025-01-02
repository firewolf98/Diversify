import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common'; // Necessario per usare i pipes come dat
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-commento',
  standalone: true,
  templateUrl: './commento.component.html',
  styleUrls: ['./commento.component.css'],
  imports: [CommonModule, FormsModule] // Importa CommonModule per abilitare il pipe date
})
export class CommentoComponent {
  @Input() comment!: { // Aggiungiamo Input per ricevere il commento dal componente padre
    authorName: string;
    authorAvatar: string;
    text: string;
    date: string;
    likes: number; // Aggiunta del contatore dei likes
    replies: { authorAvatar: string; authorName: string; text: string; date: string }[];
  };

  newComment = { text: '' }; // Inizializza il nuovo commento
  isReplying: boolean = false; // Gestisce la visualizzazione del form di risposta
  hasLiked: boolean = false;  // Traccia se l'utente ha messo "Mi Piace"
  replyText: string = '';

  onReply(): void {
    this.isReplying = !this.isReplying; // Mostra/nasconde il modulo di risposta
    if (this.isReplying) {
      this.replyText = ''; // Resetta il campo di testo quando viene mostrato il modulo
    }
  }

  likeComment(): void {
    if (this.hasLiked) {
      this.comment.likes -= 1; // Rimuovi il like
    } else {
      this.comment.likes += 1; // Aggiungi un like
    }
    this.hasLiked = !this.hasLiked; // Alterna lo stato
  }

  submitReply(replyText: string): void {
    if (replyText.trim()) {
      const newReply = {
        authorAvatar: 'Avatar.png', // Aggiungi qui l'avatar dell'autore, se necessario
        authorName: 'Utente', // Aggiungi qui il nome dell'autore, se necessario
        text: replyText,
        date: new Date().toLocaleString()
      };
      this.comment.replies.push(newReply); // Aggiungi il nuovo subcommento alla lista delle risposte
      this.isReplying = false;
    }
  }
}
