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
 
 
export const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'post', component: PostComponent },  // La nuova rotta per il componente Post
  { path: 'loggato', component: LoginFormComponent },
  { path: 'registrato', component: RegistrazioneFormComponent },
  { path: 'scheda-area-personale', component: SchedaAreaPersonaleComponent },
  { path: 'pagina-profilo-amministratore', component: PaginaProfiloAmministratoreComponent },
  { path: 'chi-siamo', component: ChiSiamoComponent },// Rotta per "Chi Siamo"
  { path: 'generale-amministratore', component: GeneraleAmministratoreComponent },// Rotta per "Generale Amministratore"
  { path: 'creapost', component: FormPostComponent }, // Rotta per creare un post
  { path: 'recupero-password', component: GestionePasswordFormComponent } ,
  { path: 'forum', component: ForumComponent}
];