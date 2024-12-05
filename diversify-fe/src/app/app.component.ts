import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MapComponent } from './mappa/mappa.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})


export class AppComponent {
  title = 'diversify-fe';
}