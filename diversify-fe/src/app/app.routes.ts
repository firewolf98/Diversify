import { Routes } from '@angular/router';
import { MapComponent } from './mappa/mappa.component';  // Import your map component

export const routes: Routes = [
  { path: '', redirectTo: '/map', pathMatch: 'full' }, // Default route to map
  { path: 'map', component: MapComponent }            // Route to the map component
];




