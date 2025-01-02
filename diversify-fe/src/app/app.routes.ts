import { Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { PostComponent } from './post/post.component';  // Assicurati di importare il tuo PostComponent
import { LoginFormComponent } from './login-form/login-form.component';
import { RegistrazioneFormComponent } from './registrazione-form/registrazione-form.component';
import { SchedaAreaPersonaleComponent } from './scheda-area-personale/scheda-area-personale.component';
import { PaginaProfiloAmministratoreComponent } from './pagina-profilo-amministratore/pagina-profilo-amministratore.component';


export const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'post', component: PostComponent },  // La nuova rotta per il componente Post
  { path: 'loggato', component: LoginFormComponent },
  { path: 'registrato', component: RegistrazioneFormComponent },
  { path: 'scheda-area-personale', component: SchedaAreaPersonaleComponent },
  { path: 'pagina-profilo-amministratore', component: PaginaProfiloAmministratoreComponent },
];
