import { Component } from '@angular/core';

// Importa il componente figlio
import { SchedaAreaPersonaleComponent } from './scheda-area-personale/scheda-area-personale.component';

@Component({
  selector: 'app-root',
  standalone: true, // Questo specifica che è un componente standalone
  imports: [SchedaAreaPersonaleComponent], // Aggiungi il componente figlio agli imports
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})


export class AppComponent {
  title = 'diversify-fe';
}
