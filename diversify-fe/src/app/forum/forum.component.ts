import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router'; 
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-forum',
  standalone: true,
  imports: [CommonModule, RouterModule], // Aggiungi CommonModule qui
  templateUrl: './forum.component.html',
  styleUrls: ['./forum.component.css']
})
export class ForumComponent {
  forums: any[] = []; // Lista dei forum
  posts: any[] = []; // Lista dei post del forum selezionato
  selectedForumId: string | null = null; // ID del forum selezionato

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadForums('Italia'); // Carica i forum del paese selezionato (puoi passare il paese dinamicamente)
  }

  // Carica i forum associati a un paese
  loadForums(paese: string): void {
    this.http.get<any[]>(`http://localhost:8080/api/forums/by-paese/${paese}`).subscribe({
      next: (data) => {
        this.forums = data; // Salva i forum caricati
        console.log('Forum caricati:', this.forums);
      },
      error: (err) => {
        console.error('Errore durante il caricamento dei forum:', err);
      },
    });
  }

  selectForum(forumId: string): void {
    const forum = this.forums.find(f => f.idForum === forumId);
    if (forum) {
      this.posts = forum.post; // Imposta i post del forum selezionato
    } else {
      this.posts = [];
    }
  }
  

  // Metodo per creare un nuovo post
  createNewPost(): void {
    if (this.selectedForumId) {
      this.router.navigate(['/creapost'], { queryParams: { forumId: this.selectedForumId } });
    } else {
      alert('Seleziona un forum prima di creare un post.');
    }
  }
}
