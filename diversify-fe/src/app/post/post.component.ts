import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommentoComponent } from '../commento/commento.component';
import { ActivatedRoute } from '@angular/router';
import { ForumService } from '../services/forum.service';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';
 
@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule, FormsModule, CommentoComponent],
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
})
export class PostComponent {
  comments: any[] = [];
 
  postLikes = 0;
  hasLikedPost = false;
  newComment = { text: '' };
 
  // Variabili per il modale di segnalazione
  showReportModal = false;
  reportReason = '';
  reportType = 'Post'; // Tipo di segnalazione (in questo caso, "post")
 
  postId: string ='';
  forumId: string | null = null;
  posts: any;
  post: any;
  autore: any;

  commentoToAdd:any;
  idUser: any;
 
  constructor(private route: ActivatedRoute, private forumService: ForumService,private userService: UserService, private authService: AuthService) {}
 
  ngOnInit(): void {
    this.postId = ''+this.route.snapshot.paramMap.get('postId');
    this.forumId = ''+this.route.snapshot.paramMap.get('selectedForumId');
    this.forumService.getPost(this.forumId).subscribe({
      next: (data) => {
        this.posts = data;
        this.post = this.posts.find((item : any) => item.idPost === this.postId);
        this.comments = this.post.commenti;
        this.postLikes = this.post.like;

        this.userService.getUtenteById(this.post.idAutore).subscribe({
          next: (user) => {
            this.autore = user;
          },
          error: (err) => {
            console.error("Errore durante il recupero dell'autore:", err);
          }
      })
      },
      error: (err) => {
        console.error("Errore durante il recupero dell'autore:", err);
      }
    });

    this.authService.getUser().subscribe({
      next: (user) => {
        this.idUser = user.idUtente;
      },
      error: (err) => {
        console.error("Errore durante il recupero dell'autore:", err);
      }
    })
  }
 
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
      this.submitReport(this.post.idAutore, this.idUser, this.reportReason, this.reportType);
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
 
  // Aggiungi un commento
  addComment() {
    if (this.newComment.text.trim()) {
      this.commentoToAdd = {
        idAutore: this.idUser,
        contenuto: this.newComment.text,
        idPost: this.postId
      };
      this.newComment.text = '';
    }
    this.forumService.aggiungiCommento(this.postId,this.commentoToAdd).subscribe({
      next: (data) => {
        this.comments.push(data);
      },
      error: (err) => {
        console.error("Errore:", err);
      }
    });
  }
 
  // Mi Piace per il post
  toggleLikePost() {
    this.hasLikedPost = !this.hasLikedPost;
    this.post.like += this.hasLikedPost ? 1 : -1;
    this.forumService.addLikeToPost(this.postId).subscribe({
      next: (data) => {
        
      },
      error: (err) => {
        console.error("Errore:", err);
      }
    });
  }
 
  // Mi Piace per i commenti
  likeComment(comment: any) {
    comment.likes++;
  }
}