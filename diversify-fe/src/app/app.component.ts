import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./header/header.component";
import { FooterComponent } from './footer/footer.component';
import { GeneraleAmministratoreComponent } from "./generale-amministratore/generale-amministratore.component";
@Component({
	selector: 'app-root',
	standalone: true,
	imports: [RouterOutlet, HeaderComponent, FooterComponent, GeneraleAmministratoreComponent],
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css'],

})

export class AppComponent {
	title = 'diversify-fe';
}