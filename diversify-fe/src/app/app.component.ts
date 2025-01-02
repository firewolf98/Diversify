import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./header/header.component";
import { FooterComponent } from "./footer/footer.component";
import { PopupGridComponent } from './popup-grid/popup-grid.component'; // Percorso corretto
import { PostComponent } from './post/post.component';

import { SchedaAreaPersonaleComponent } from "./scheda-area-personale/scheda-area-personale.component";
import { LoginFormComponent } from "./login-form/login-form.component";
import { RegistrazioneFormComponent } from "./registrazione-form/registrazione-form.component";
import { MapComponent } from "./mappa/mappa.component";
import { CommentoComponent } from './commento/commento.component';
import { ForumComponent } from "./forum/forum.component";

@Component({
	selector: 'app-root',
	standalone: true,
	imports: [RouterOutlet, HeaderComponent, FooterComponent ],
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css'],

})

export class AppComponent {
	title = 'diversify-fe';
}