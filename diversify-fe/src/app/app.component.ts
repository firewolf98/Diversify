import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
<<<<<<< HEAD
@Component({
  selector: 'app-root',
  standalone: true, // Se utilizzi moduli standalone
  imports: [RouterOutlet], 
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'], // Corretto styleUrls
=======
import { MapComponent } from './mappa/mappa.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
>>>>>>> origin/develop
})
export class AppComponent {
  title = 'diversify-fe';
}