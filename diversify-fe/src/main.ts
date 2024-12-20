import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { GeneraleAmministratoreComponent } from './app/generale-amministratore/generale-amministratore.component';


import 'ol/ol.css';
bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
  bootstrapApplication(GeneraleAmministratoreComponent, appConfig)
  .catch((err) => console.error(err));