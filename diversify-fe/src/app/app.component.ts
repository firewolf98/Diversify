import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./header/header.component";
import { FooterComponent } from "./footer/footer.component";
import { GestionePasswordFormComponent } from "./gestione-password-form/gestione-password-form.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent, GestionePasswordFormComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'], 
})


export class AppComponent {
  title = 'diversify-fe';
}