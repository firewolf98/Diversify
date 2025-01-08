import { Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { PostComponent } from './post/post.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { RegistrazioneFormComponent } from './registrazione-form/registrazione-form.component';
import { SchedaAreaPersonaleComponent } from './scheda-area-personale/scheda-area-personale.component';
import { PaginaProfiloAmministratoreComponent } from './pagina-profilo-amministratore/pagina-profilo-amministratore.component';
import { ChiSiamoComponent } from './chi-siamo/chi-siamo.component';
import { GeneraleAmministratoreComponent } from './generale-amministratore/generale-amministratore.component';
import { FormPostComponent } from './form-post/form-post.component';
import { GestionePasswordFormComponent } from './gestione-password-form/gestione-password-form.component';
import { ForumComponent } from './forum/forum.component';
import { ForumAccessGuard } from './guards/forum-access.guard'; // Aggiungi la guardia per i forum
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomepageComponent },
 
  //Rotta per mostrare il post del forum
  { 
    path: 'post/:postId/:selectedForumId', 
    component: PostComponent, 
    canActivate: [ForumAccessGuard] // Protegge l'accesso ai post
  },

  { 
    path: 'loggato', 
    component: LoginFormComponent, 
    canActivate: [AuthGuard] // Protegge l'accesso alla pagina di login 
  },

  { path: 'registrato', component: RegistrazioneFormComponent },
  { path: 'scheda-area-personale', component: SchedaAreaPersonaleComponent },
  { path: 'pagina-profilo-amministratore', component: PaginaProfiloAmministratoreComponent },
  { path: 'chi-siamo', component: ChiSiamoComponent },
  { path: 'generale-amministratore', component: GeneraleAmministratoreComponent },
  { path: 'creapost', component: FormPostComponent },
  { path: 'recupero-password', component: GestionePasswordFormComponent },
  
  // Rotta per mostrare tutti i forum
  { 
    path: 'forum', 
    component: ForumComponent, 
    canActivate: [ForumAccessGuard] // Protegge l'accesso ai forum
  },

  // Rotta per mostrare un forum specifico in evidenza
  { 
    path: 'forum/:forumId', 
    component: ForumComponent, 
    canActivate: [ForumAccessGuard] // Protegge l'accesso ai forum specifici
  },
];
