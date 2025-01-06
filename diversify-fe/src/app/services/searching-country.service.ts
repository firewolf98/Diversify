import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchingCountryService {
  // Subject per emettere l'evento del paese selezionato
  private selectedCountrySource = new Subject<string>();
  private resetMapSource = new Subject<void>();

  // Observable che i componenti possono sottoscrivere per ricevere il paese selezionato
  selectedCountry$ = this.selectedCountrySource.asObservable();
  resetMap$ = this.resetMapSource.asObservable();

  constructor() {}

  // Metodo per emettere il paese selezionato
  selectCountry(country: string): void {
    this.selectedCountrySource.next(country);
  }

  resetMap(): void {
    this.resetMapSource.next(); // Emissione dell'evento di reset
  }
}
