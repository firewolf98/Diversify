import { Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { PostComponent } from './post/post.component';  // Assicurati di importare il tuo PostComponent


export const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'post', component: PostComponent }   // La nuova rotta per il componente Post
];
