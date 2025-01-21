import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { ForumService } from '../services/forum.service';

@Component({
  selector: 'app-gestione-forum',
  standalone: true,  // Questo lo rende un componente standalone
  imports: [CommonModule, ReactiveFormsModule],  // Le dipendenze necessarie per il componente
  templateUrl: './gestione-forum.component.html',
  styleUrls: ['./gestione-forum.component.css']
})
export class GestioneForumComponent implements OnInit {
  forumForm: FormGroup;
  forums: any[] = [];
  editingForumId: string | null = null;
  ruolo: boolean = true; // Supponiamo che l'utente sia amministratore, puoi modificarlo dinamicamente
  newForum = { titolo: '', descrizione: '', paese: '' };

  countries = [
    'Austria', 'Belgio', 'Bulgaria', 'Cipro', 'Croazia', 'Danimarca',
    'Estonia', 'Finlandia', 'Francia', 'Germania', 'Grecia', 'Irlanda',
    'Italia', 'Lettonia', 'Lituania', 'Lussemburgo', 'Malta',
    'Paesi Bassi', 'Polonia', 'Portogallo', 'Repubblica Ceca',
    'Romania', 'Slovacchia', 'Slovenia', 'Spagna', 'Svezia'
  ];

  constructor(
    private fb: FormBuilder,
    private cd: ChangeDetectorRef,
    private forumService: ForumService
  ) {
    this.forumForm = this.fb.group({
      paese: ['', Validators.required],
      titolo: ['', [Validators.required, Validators.minLength(3)]],
      descrizione: ['', [Validators.required, Validators.minLength(10)]],
    });
  }

  ngOnInit(): void {
    this.loadForums();
  }

  loadForums(): void {
    this.forumService.getAllForums().subscribe((forums) => {
      console.log(forums); // Verifica se i forum vengono caricati
      this.forums = forums;
    });
  }

  createForum(): void {
    if (this.newForum.titolo && this.newForum.descrizione && this.newForum.paese) {
      this.forumService.createForum(this.newForum).subscribe(
        (response) => {
          console.log('Forum creato:', response);  // Verifica la risposta
          alert('Forum creato con successo!');
          this.newForum = { titolo: '', descrizione: '', paese: '' }; // Reset dei dati
        },
        (error) => {
          console.error('Errore nella creazione del forum:', error);
          alert('Errore nella creazione del forum!');
        }
      );
    } else {
      alert('Compila tutti i campi!');
    }
  }
  
  onSubmit(): void {
    if (this.forumForm.valid) {
      const forum = this.forumForm.value;
  
      if (this.editingForumId === null) {
        // Creazione di un nuovo forum
        this.forumService.addForum(forum, this.ruolo).subscribe((newForum) => {
          this.forums.push(newForum);
          this.forumForm.reset();
        });
      } else {
        // Modifica di un forum esistente
        this.forumService.updateForum(this.editingForumId, forum, this.ruolo).subscribe((updatedForum) => {
          const index = this.forums.findIndex(f => f.id === this.editingForumId);
          if (index !== -1) {
            this.forums[index] = updatedForum;
            console.log('Lista dei forum aggiornata:', this.forums);  // Verifica la lista aggiornata
          }
          this.editingForumId = null;
          this.forumForm.reset();
          this.cd.detectChanges();  // Forza un controllo manuale dei cambiamenti
        });
        
      }
    }
  }

  editForum(id: string): void {
    console.log(id); // Verifica se l'ID viene passato correttamente
    this.editingForumId = id;
    this.forumService.getForumById(id).subscribe((forum) => {
      this.forumForm.setValue({
        paese: forum.paese,  // Assicurati che questi siano corretti
        titolo: forum.titolo,
        descrizione: forum.descrizione,
      });
    });
  }  
  
  deleteForum(id: string): void {
    const confirmDelete = window.confirm('Sei sicuro di voler eliminare questo forum?');
    if (confirmDelete) {
      this.forumService.deleteForum(id, this.ruolo).subscribe(() => {
        this.forums = this.forums.filter((forum) => forum.id !== id);
      });
    }
  }

  hasError(controlName: string, errorCode: string): boolean {
    const control = this.forumForm.get(controlName);
    return !!((control?.touched || control?.dirty) && control?.hasError(errorCode));
  }
}
