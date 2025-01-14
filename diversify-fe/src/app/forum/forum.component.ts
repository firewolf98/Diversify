import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { RouterModule } from '@angular/router';
import { ForumService } from '../services/forum.service';
 
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
  countryName: string = 'Italia';
 
  constructor(private route: ActivatedRoute,private http: HttpClient, private router: Router,private forumService: ForumService) {}
 
  ngOnInit(): void {
    // Recupera query parameters (country e forumId)
    this.route.queryParams.subscribe(params => {
      this.countryName = params['country'] || 'Italia'; // Default 'Italia'
      this.selectedForumId = params['forumId'] || null;
      // Carica i dati
      this.loadForums(this.countryName);
     
    });
  }
 
  // Carica i forum associati a un paese
  loadForums(paese: string): void {
    this.forumService.loadForums(paese).subscribe(data => {
      this.forums = data;
      if (this.selectedForumId) {
        this.selectForum(this.selectedForumId);
      }
    });
  }
 
  selectForum(forumId: string): void {
    this.selectedForumId = forumId;
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
 
  navigateToPost(postId: string): void {
    this.router.navigate(['/post', postId, this.selectedForumId]);
  }
 
}