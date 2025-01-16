import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';
import { ForumService } from '../services/forum.service';

@Component({
  selector: 'app-commento',
  standalone: true,
  templateUrl: './commento.component.html',
  styleUrls: ['./commento.component.css'],
  imports: [CommonModule, FormsModule],
})
export class CommentoComponent {
  @Input() comment:any;

  authorName: string | undefined;
  idUser: any;

  constructor(private utenteService: UserService, private authService: AuthService, private forumService: ForumService) {} // Iniezione del servizio

  ngOnInit() {
    this.fetchAuthorName();

    this.authService.getUser().subscribe({
      next: (user) => {
        this.idUser = user.idUtente;
      },
      error: (err) => {
        console.error("Errore durante il recupero dell'autore:", err);
      }
    });

    this.loadSubCommentAuthors();
  }

  fetchAuthorName() {
    if (this.comment.idAutore) {
      this.utenteService.getUtenteById(this.comment.idAutore).subscribe({
        next: (user) => {
          this.authorName = user.nome;
        },
        error: (err) => {
          console.error("Errore nel recupero dell'utente:", err);
        }
      });
    }
  }
  newComment = { text: '' };
  isReplying: boolean = false;
  hasLiked: boolean = false;
  replyText: string = '';
  replyToAdd: any;
  reportType = 'Commento';

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

  confirmReport() {
    if (this.reportReason.trim()) {
      this.submitReport(this.comment.idAutore, this.idUser, this.reportReason, this.reportType);
      this.closeReportModal();
    } else {
      alert('Per favore, inserisci una motivazione.');
    }
  }
 
  // Funzione per inviare la segnalazione
  submitReport(reportedUser: string, reportingUser: string, reason: string, type: string) {
    const reportData = {
      idSegnalato: reportedUser, // Username segnalato
      idSegnalante: reportingUser, // Username segnalante
      motivazione: reason, // Motivazione della segnalazione
      tipoSegnalazione: type // Tipo di segnalazione (es. "post")
    };

    this.forumService.addReport(reportData).subscribe({
      next: (data) => {
        alert("Segnalazione inviata");
      },
      error: (err) => {
        console.error("Errore:", err);
      }
    });
  }

  onReply(): void {
    this.isReplying = !this.isReplying;
    if (this.isReplying) {
      this.replyText = '';
    }
  }

  likeComment(): void {
    if (this.hasLiked) {
      this.comment.like -= 1;
    } else {
      this.comment.like += 1;
    }
    this.hasLiked = !this.hasLiked;
    this.forumService.addLikeToCommento(this.comment.idCommento).subscribe({
      next: (data) => {

      },
      error: (err) => {
        console.error("Errore:", err);
      }
    });
  }

  submitReply(replyText: string): void {
    if (replyText.trim()) {
      this.replyToAdd = {
        idAutore: this.idUser,
        contenuto: replyText
      };
      this.replyText = '';
    }
    this.forumService.aggiungiSubCommento(this.comment.idCommento,this.replyToAdd).subscribe({
      next: (data) => {
        this.isReplying = false;
        this.comment.subcommenti.push(data);
        this.loadSubCommentAuthors();
      },
      error: (err) => {
        console.error("Errore:", err);
      }
    });
  }

  loadSubCommentAuthors() {
    if (this.comment.subcommenti && this.comment.subcommenti.length > 0) {
      this.comment.subcommenti.forEach((subcomment: any) => {
        if (subcomment.idAutore) {
          this.utenteService.getUtenteById(subcomment.idAutore).subscribe({
            next: (user) => {
              subcomment.authorName = user.nome; // Aggiunge il nome dell'autore al subcommento
            },
            error: (err) => {
              console.error("Errore nel recupero del nome dell'autore del subcommento:", err);
            }
          });
        }
      });
    }
  }
}