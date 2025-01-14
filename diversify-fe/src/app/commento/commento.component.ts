import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-commento',
  standalone: true,
  templateUrl: './commento.component.html',
  styleUrls: ['./commento.component.css'],
  imports: [CommonModule, FormsModule],
})
export class CommentoComponent {
  @Input() comment!: {
    authorName: string;
    authorAvatar: string;
    text: string;
    date: string;
    likes: number;
    replies: { authorAvatar: string; authorName: string; text: string; date: string }[];
  };

  newComment = { text: '' };
  isReplying: boolean = false;
  hasLiked: boolean = false;
  replyText: string = '';

  // Variabili per il modale di segnalazione
  showReportModal = false;
  reportReason = '';
  reportedComment: any = null; // Memorizza il commento o il subcommento segnalato

  // Variabile per il messaggio di conferma
  showConfirmation = false;

  // Apre il modale di segnalazione
  openReportModal(comment: any) {
    this.reportedComment = comment; // Memorizza il commento o il subcommento segnalato
    this.showReportModal = true;
  }

  // Chiude il modale di segnalazione
  closeReportModal() {
    this.showReportModal = false;
    this.reportReason = ''; // Resetta il campo di testo
    this.reportedComment = null; // Resetta il commento segnalato
  }

  // Conferma la segnalazione
  confirmReport() {
    if (this.reportReason.trim() && this.reportedComment) {
      console.log('Segnalazione inviata:', {
        reportedUser: this.reportedComment.authorName, // Usa l'autore del commento/subcommento segnalato
        reportingUser: 'UtenteCorrente', // Esempio: utente loggato
        reason: this.reportReason,
        type: 'comment' // Puoi distinguere tra "comment" e "reply" se necessario
      });

      // Mostra il messaggio di conferma
      this.showConfirmation = true;

      // Nascondi il messaggio dopo 3 secondi
      setTimeout(() => {
        this.showConfirmation = false;
      }, 3000);

      this.closeReportModal();
    } else {
      alert('Per favore, inserisci una motivazione.');
    }
  }

  onReply(): void {
    this.isReplying = !this.isReplying;
    if (this.isReplying) {
      this.replyText = '';
    }
  }

  likeComment(): void {
    if (this.hasLiked) {
      this.comment.likes -= 1;
    } else {
      this.comment.likes += 1;
    }
    this.hasLiked = !this.hasLiked;
  }

  submitReply(replyText: string): void {
    if (replyText.trim()) {
      const newReply = {
        authorAvatar: 'Avatar.png',
        authorName: 'Utente',
        text: replyText,
        date: new Date().toLocaleString(),
      };
      this.comment.replies.push(newReply);
      this.isReplying = false;
    }
  }
}