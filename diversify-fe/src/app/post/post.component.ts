import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommentoComponent } from '../commento/commento.component';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule, FormsModule, CommentoComponent],
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
})

export class PostComponent {
  comments = [
    {
      authorName: 'Mario Rossi',
      authorAvatar: 'Avatar.png',
      text: 'Questo è un commento di esempio.',
      date: '2025-01-02',
      likes: 5,
      replies: [
        { authorAvatar: 'Avatar.png', authorName: 'Luigi Verdi', text: 'Ecco un subcommento!', date: '2025-01-02' },
        { authorAvatar: 'Avatar.png', authorName: 'Giovanni Bianchi', text: 'Risposta al subcommento!', date: '2025-01-02' }
      ]
    },
    {
      authorName: 'Giulia Verdi',
      authorAvatar: 'Avatar.png',
      text: 'Sono d’accordo! Grazie per aver condiviso.',
      date: '2024-12-18T12:00:00Z',
      likes: 0,
      replies: [] // Aggiungi questa proprietà anche ai commenti senza risposte
    },
  ];

  postLikes = 0; // Conteggio dei "Mi Piace" per il post principale
  hasLikedPost = false; // Stato iniziale: l'utente non ha messo "Mi Piace"

  newComment = { text: '' };

  addComment() {
    if (this.newComment.text.trim()) {
      const newComment = {
        authorName: 'Nuovo Utente',
        authorAvatar: 'Avatar.png',
        text: this.newComment.text,
        date: new Date().toISOString(),
        likes: 0, // Inizializza i "Mi Piace" a 0
        replies: [] // Assicurati che i nuovi commenti abbiano sempre l'array replies
      };
      this.comments.push(newComment);
      this.newComment.text = '';
    }
  }

  toggleLikePost() {
    this.hasLikedPost = !this.hasLikedPost; // Alterna lo stato
    this.postLikes += this.hasLikedPost ? 1 : -1; // Incrementa o decrementa i "Mi Piace"
  }

  likeComment(comment: any) {
    comment.likes++; // Incrementa i "Mi Piace" del commento specifico
  }
}
