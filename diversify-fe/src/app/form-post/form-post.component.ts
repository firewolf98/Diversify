import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service'; // Importa il servizio di autenticazione

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

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.moduloPost = this.fb.group({
      titolo: ['', [Validators.required, Validators.minLength(5)]],
      contenuto: ['', [Validators.required, Validators.minLength(20)]],
    });
  }
  inviaPost(): void {
    this.clearMessages(); // Resetta eventuali messaggi precedenti

    if (this.moduloPost.valid) {
      const author = this.authService.getLoggedUsername();
      if (!author) {
        this.errorMessage = 'Errore: Utente non loggato.';
        return;
      }

      const postData = {
        ...this.moduloPost.value,
        author, // Aggiunge l'autore ai dati del post
      };

      this.authService.savePost(postData).subscribe({
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
