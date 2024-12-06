import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet], // Corretto: separa i componenti con una virgola
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'], // Corretto: usa `styleUrls` al plurale
})
export class AppComponent {
  title = 'diversify-fe';
}