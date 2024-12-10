import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SchedaAreaPersonaleComponent } from "./scheda-area-personale/scheda-area-personale.component";

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