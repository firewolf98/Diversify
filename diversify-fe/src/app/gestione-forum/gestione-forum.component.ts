import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-gestione-forum',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './gestione-forum.component.html',
  styleUrls: ['./gestione-forum.component.css']
})
export class GestioneForumComponent {
  forumForm: FormGroup;  // FormGroup per gestire il form del forum
  errorMessage: string = '';  // Messaggio di errore per il form

  countries = [
    'Austria', 'Belgio', 'Bulgaria', 'Cipro', 'Croazia', 'Danimarca', 'Estonia', 'Finlandia', 'Francia', 
    'Germania', 'Grecia', 'Irlanda', 'Italia', 'Lettonia', 'Lituania', 'Lussemburgo', 'Malta', 'Paesi Bassi', 
    'Polonia', 'Portogallo', 'Repubblica Ceca', 'Romania', 'Slovacchia', 'Slovenia', 'Spagna', 'Svezia'
  ];

  posts: any[] = []; // Lista di post creati

  constructor(private fb: FormBuilder) {
    // Inizializza il form con i campi e le loro convalide
    this.forumForm = this.fb.group({
      country: ['', Validators.required], // Paese è obbligatorio
      title: ['', [Validators.required, Validators.minLength(3)]], // Titolo obbligatorio, almeno 3 caratteri
      description: ['', [Validators.required, Validators.minLength(10)]] // Descrizione obbligatoria, almeno 10 caratteri
    });
  }

  // Funzione chiamata quando l'utente invia il form
  onSubmit(): void {
    if (this.forumForm.valid) {  // Se il form è valido
      const newForum = {
        country: this.forumForm.get('country')?.value,
        title: this.forumForm.get('title')?.value,
        description: this.forumForm.get('description')?.value
      };

      console.log('Forum creato:', newForum);
      this.posts.push(`${newForum.country} - ${newForum.title}: ${newForum.description}`);

      // Reset del form dopo l'invio
      this.forumForm.reset();
      this.errorMessage = '';  // Rimuove eventuali messaggi di errore
    } else {
      this.errorMessage = 'Per favore, compila tutti i campi correttamente.';
    }
  }

  // Funzione per verificare se un campo ha un errore specifico
  hasError(controlName: string, errorCode: string): boolean {
    const control = this.forumForm.get(controlName);
    return ((control?.touched || control?.dirty) && control?.hasError(errorCode)) ?? false;
  }  
}  