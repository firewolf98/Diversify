import { Component, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Elenco dei Paesi dell'Unione Europea
const countriesDB = [
  'Austria', 'Belgio', 'Bulgaria', 'Cipro', 'Croazia', 'Danimarca', 'Estonia', 'Finlandia', 'Francia', 
  'Germania', 'Grecia', 'Irlanda', 'Italia', 'Lettonia', 'Lituania', 'Lussemburgo', 'Malta', 'Paesi Bassi', 
  'Polonia', 'Portogallo', 'Repubblica Ceca', 'Romania', 'Slovacchia', 'Slovenia', 'Spagna', 'Svezia'
];

@Component({
  selector: 'app-search-bar',
  standalone: true,
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css'],
  imports: [CommonModule, FormsModule]  // Importa CommonModule e FormsModule
})
export class SearchBarComponent implements OnInit {
  searchTerm: string = '';
  filteredCountries: string[] = [];
  isDropdownVisible: boolean = false;  // Variabile per controllare la visibilità della tendina

  ngOnInit(): void {
    this.filteredCountries = countriesDB;
  }

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const searchValue = input.value.trim().toLowerCase();

    // Mostra la tendina solo se l'utente ha digitato qualcosa
    this.isDropdownVisible = searchValue.length > 0;

    if (searchValue.length === 0) {
      this.filteredCountries = [];  // Non mostrare nessun risultato se la barra è vuota
      return;
    }

    // Filtra i Paesi in base alla logica migliorata
    this.filteredCountries = countriesDB
      .filter(country => {
        const countryLower = country.toLowerCase();

        // 1. Prima ricerca per parole che cominciano con il termine digitato
        // 2. Poi, aggiungi Paesi che contengono il termine ma non all'inizio
        return countryLower.startsWith(searchValue) || countryLower.includes(searchValue);
      })
      .sort((a, b) => {
        const searchValueLength = searchValue.length;

        // Ordina per rilevanza: prima i Paesi che iniziano con il termine
        if (a.toLowerCase().startsWith(searchValue) && !b.toLowerCase().startsWith(searchValue)) {
          return -1;
        }
        if (!a.toLowerCase().startsWith(searchValue) && b.toLowerCase().startsWith(searchValue)) {
          return 1;
        }

        // Se entrambi i Paesi iniziano o contengono il termine, ordina per posizione nel nome
        return a.toLowerCase().indexOf(searchValue) - b.toLowerCase().indexOf(searchValue);
      });
  }

  toggleDropdown(): void {
    this.isDropdownVisible = !this.isDropdownVisible;  
  }

  selectCountry(country: string): void {
    this.searchTerm = country; 
    this.filteredCountries = []; 
    this.isDropdownVisible = false;  
  }

  // Aggiungi il listener per il clic fuori dalla barra di ricerca
  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    const clickedInside = (event.target as HTMLElement).closest('.search-container');
    
    if (!clickedInside) {
      this.isDropdownVisible = false; // Nascondi il dropdown se clicchi fuori
  }
 }
}
