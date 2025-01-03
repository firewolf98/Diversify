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
      replies: []
    },
  ];

  postLikes = 0;
  hasLikedPost = false;
  newComment = { text: '' };

  // Variabili per il modale di segnalazione
  showReportModal = false;
  reportReason = '';

  // Dati della segnalazione
  reportedUser = 'Lucia'; // Username segnalato (esempio: autore del post)
  reportingUser = 'UtenteCorrente'; // Username segnalante (esempio: utente loggato)
  reportType = 'post'; // Tipo di segnalazione (in questo caso, "post")

  // Apre il modale di segnalazione
  openReportModal() {
    this.showReportModal = true;
  }

  // Chiude il modale di segnalazione
  closeReportModal() {
    this.showReportModal = false;
    this.reportReason = ''; // Resetta il campo di testo
  }

  // Conferma la segnalazione
  confirmReport() {
    if (this.reportReason.trim()) {
      this.submitReport(this.reportedUser, this.reportingUser, this.reportReason, this.reportType);
      this.closeReportModal();
    } else {
      alert('Per favore, inserisci una motivazione.');
    }
  }

  // Funzione per inviare la segnalazione
  submitReport(reportedUser: string, reportingUser: string, reason: string, type: string) {
    const reportData = {
      reportedUser: reportedUser, // Username segnalato
      reportingUser: reportingUser, // Username segnalante
      reason: reason, // Motivazione della segnalazione
      type: type // Tipo di segnalazione (es. "post")
    };

    // Stampa i dati della segnalazione in console
    console.log('Segnalazione inviata:', reportData);

    // Qui puoi aggiungere la logica per inviare i dati a un servizio backend
    // Esempio: this.reportService.submitReport(reportData).subscribe(...);
  }

  // Aggiungi un commento
  addComment() {
    if (this.newComment.text.trim()) {
      const newComment = {
        authorName: 'Nuovo Utente',
        authorAvatar: 'Avatar.png',
        text: this.newComment.text,
        date: new Date().toISOString(),
        likes: 0,
        replies: []
      };
      this.comments.push(newComment);
      this.newComment.text = '';
    }
  }

  // Mi Piace per il post
  toggleLikePost() {
    this.hasLikedPost = !this.hasLikedPost;
    this.postLikes += this.hasLikedPost ? 1 : -1;
  }

  // Mi Piace per i commenti
  likeComment(comment: any) {
    comment.likes++;
  }
}