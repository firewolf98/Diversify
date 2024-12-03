import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
@Component({
  selector: 'app-root',
  standalone: true, // Se utilizzi moduli standalone
  imports: [RouterOutlet], 
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'], // Corretto styleUrls
})
export class AppComponent {
  title = 'diversify-fe';
}
