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
import { ForumAccessGuard } from './guards/forum-access.guard'; 
import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';
import { CampagnaCrowdfundingComponent } from './campagna-crowdfunding/campagna-crowdfunding.component';


export const routes: Routes = [
  { path: '', component: HomepageComponent },
 
  
  { 
    path: 'post/:postId/:selectedForumId', 
    component: PostComponent, 
    canActivate: [ForumAccessGuard] 
  },

  { 
    path: 'loggato', 
    component: LoginFormComponent, 
    canActivate: [AuthGuard]
  },

  { 
    path: 'registrato',
     component: RegistrazioneFormComponent,
     canActivate: [AuthGuard]
  },

  {
    path: 'scheda-area-personale',
    component: SchedaAreaPersonaleComponent,
    canActivate: [AuthGuard]
   },

   {
    path: 'pagina-profilo-amministratore',
    component: PaginaProfiloAmministratoreComponent,
    canActivate: [AdminGuard], 
  },

  {
    path: 'generale-amministratore',
    component: GeneraleAmministratoreComponent,
    canActivate: [AdminGuard], 
  },

  { 
    path: 'chi-siamo',
    component: ChiSiamoComponent
  },

  { 
     path: 'creapost',
     component: FormPostComponent,
     canActivate: [ForumAccessGuard]
  },

  { 
    path: 'recupero-password', 
    component: GestionePasswordFormComponent,
    canActivate: [AuthGuard]
  },
  
  { 
    path: 'forum', 
    component: ForumComponent, 
    canActivate: [ForumAccessGuard] 
  },

  { 
    path: 'forum/:forumId', 
    component: ForumComponent, 
    canActivate: [ForumAccessGuard] 
  },

  {
    path: 'campagne/:paese', 
    component: CampagnaCrowdfundingComponent,
    canActivate: [AuthGuard], 
  },
];
