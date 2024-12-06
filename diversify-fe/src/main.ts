import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { SchedaAreaPersonaleComponent } from './app/scheda-area-personale/scheda-area-personale.component';
import 'ol/ol.css';
bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));