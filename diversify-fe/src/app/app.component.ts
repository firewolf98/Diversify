import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MapComponent } from './mappa/mappa.component';
import { SchedaAreaPersonaleComponent } from "./scheda-area-personale/scheda-area-personale.component";
import { RegistrazioneFormComponent } from "./registrazione-form/registrazione-form.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SchedaAreaPersonaleComponent, RegistrazioneFormComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})


export class AppComponent {
  title = 'diversify-fe';
}