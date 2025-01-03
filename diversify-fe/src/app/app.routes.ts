import { Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { PostComponent } from './post/post.component';  
import { ChiSiamoComponent } from './chi-siamo/chi-siamo.component';


export const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'post', component: PostComponent },   // La nuova rotta per il componente Post
  { path: 'chi-siamo', component: ChiSiamoComponent }// Rotta per "Chi Siamo"
];
