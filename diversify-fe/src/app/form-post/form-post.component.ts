import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service'; // Importa il servizio di autenticazione
import { ForumService } from '../services/forum.service';

@Component({
  selector: 'app-form-post',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form-post.component.html',
  styleUrls: ['./form-post.component.css'],
})
export class FormPostComponent {
  moduloPost: FormGroup;
  errorMessage: string | null = null; // Variabile per messaggi di errore
  successMessage: string | null = null; // Variabile per messaggi di successo
  idForum: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private forumService: ForumService) {
    this.moduloPost = this.fb.group({
      titolo: ['', [Validators.required, Validators.minLength(5)]],
      contenuto: ['', [Validators.required, Validators.minLength(20)]],
    });

    const queryParams = new URLSearchParams(window.location.search);
    this.idForum = ''+queryParams.get('forumId');
  }
  inviaPost(): void {
    this.clearMessages(); // Resetta eventuali messaggi precedenti

    if (this.moduloPost.valid) {
      let idAutore = '';
      this.authService.getUser().subscribe({
        next: (author) => {
          if (author) {
            idAutore = author.idUtente; // Ora puoi accedere a `id`
          } else {
            console.error("Nessun autore trovato");
          }
        },
        error: (err) => {
          console.error("Errore durante il recupero dell'autore:", err);
        }
      });

      const idForum = this.idForum;

      const postData = {
        ...this.moduloPost.value,
        idForum,
        idAutore, // Aggiunge l'autore ai dati del post
      };
      console.log("STAMPA",postData);
      this.forumService.savePost(postData).subscribe({
        next: (response) => {
          console.log('Post salvato con successo:', response);
          this.successMessage = 'Post pubblicato con successo!';
          this.moduloPost.reset();
        },
        error: (error) => {
          console.error('Errore durante la pubblicazione del post:', error);
          this.errorMessage = 'Errore durante la pubblicazione del post.';
        },
      });
    } else {
      this.errorMessage = 'Compila tutti i campi correttamente prima di pubblicare.';
    }
  }

  private clearMessages(): void {
    this.errorMessage = null;
    this.successMessage = null;
  }

}