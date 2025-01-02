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
  selector: 'app-segnalazione-paese',
  templateUrl: './segnalazione-paese.component.html',
  styleUrls: ['./segnalazione-paese.component.css'],
  standalone: true,  // Componente stand-alone
  imports: [CommonModule, FormsModule],  // Includi FormsModule per l'uso di ngModel
})
export class SegnalazionePaeseComponent implements OnInit {
  countries = countriesDB;
  selectedCountry = 'Italia';  // Paese predefinito
  searchTerm: string = '';
  filteredCountries: string[] = [];
  isDropdownVisible: boolean = false;

  humanRightsBenchmark = 2;
  lgbtRightsBenchmark = 1;
  disabilityRightsBenchmark = 2;
  racismRightsBenchmark = 3;
  womenRightsBenchmark = 1;
  humanRightsDescription: string = '';
  
  ngOnInit(): void {
    this.filteredCountries = this.countries;
  }

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const searchValue = input.value.trim().toLowerCase();

    this.isDropdownVisible = searchValue.length > 0;

    if (searchValue.length === 0) {
      this.filteredCountries = [];  // Non mostrare nessun risultato se la barra è vuota
      return;
    }

    this.filteredCountries = this.countries
      .filter(country => country.toLowerCase().includes(searchValue))
      .sort((a, b) => a.toLowerCase().indexOf(searchValue) - b.toLowerCase().indexOf(searchValue));
  }

  toggleDropdown(): void {
    this.isDropdownVisible = !this.isDropdownVisible;
  }

  

  selectCountry(country: string): void {
    this.selectedCountry = country;
    this.searchTerm = country;
    this.filteredCountries = [];
    this.isDropdownVisible = false;
  }

  onBenchmarkChange(value: number, type: string): void {
    // Gestisci il cambiamento del benchmark in base al tipo
    switch (type) {
      case 'human':
        this.humanRightsBenchmark = value;
        break;
      case 'lgbt':
        this.lgbtRightsBenchmark = value;
        break;
      case 'disability':
        this.disabilityRightsBenchmark = value;
        break;
      case 'racism':
        this.racismRightsBenchmark = value;
        break;
      case 'women':
        this.womenRightsBenchmark = value;
        break;
      default:
        break;
    }
  }  

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    const clickedInside = event.target instanceof HTMLElement && event.target.closest('.search-container');
    if (!clickedInside) {
      this.isDropdownVisible = false;
    }
  }

  getBenchmarkComment(value: number): string {
    switch (value) {
        case 0:
            return 'Eccellente';
        case 1:
            return 'Buono';
        case 2:
            return 'Sufficiente';
        case 3:
            return 'Mediocre';
        case 4:
            return 'Scarso';
        case 5:
            return 'Pessimo';
        default:
            return '';
    }
}
}
