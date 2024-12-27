import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommentoComponent } from '../commento/commento.component';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
})
export class PostComponent {
  comments = [
    {
      authorName: 'Mario Rossi',
      authorAvatar: 'Avatar.png', // URL dell'immagine
      text: 'Ottimo post! Molto interessante.',
      date: '2024-12-18T10:00:00Z',
    },
    {
      authorName: 'Giulia Verdi',
      authorAvatar: 'Avatar.png',
      text: 'Sono d’accordo! Grazie per aver condiviso.',
      date: '2024-12-18T12:00:00Z',
    },
  ];

  newComment = { text: '' };

  addComment() {
    if (this.newComment.text.trim()) {
      const newComment = {
        authorName: 'Nuovo Utente',
        authorAvatar: 'Avatar.png', // Avatar predefinito
        text: this.newComment.text,
        date: new Date().toISOString(),
      };
      this.comments.push(newComment);
      this.newComment.text = ''; // Pulisce il campo dopo l'invio
    }
  }
}

