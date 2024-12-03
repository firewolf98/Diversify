import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component'; // Percorso corretto al file HeaderComponent

@Component({
  selector: 'app-root',
  standalone: true, // Se utilizzi moduli standalone
  imports: [RouterOutlet, HeaderComponent], // Aggiungi il componente Header
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'], // Corretto styleUrls
})
export class AppComponent {
  title = 'diversify-fe';
}
