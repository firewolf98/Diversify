import { Component, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { FormsModule } from '@angular/forms';
import { CountryService } from '../services/country.service';

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
  paeseToUpdate: any;
  humanRightsBenchmark = 0;
  lgbtRightsBenchmark = 0;
  disabilityRightsBenchmark = 0;
  racismRightsBenchmark = 0;
  womenRightsBenchmark = 0;
  humanRightsDescription: string = '';

  constructor(private countryService: CountryService) {

  }
  
  ngOnInit(): void {
    this.filteredCountries = this.countries;
  }

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const searchValue = input.value.trim().toLowerCase();

    this.isDropdownVisible = searchValue.length > 0;

    if (searchValue.length === 0) {
      this.filteredCountries = [];  // Non mostrare nessun risultato se la barra Ã¨ vuota
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

    this.countryService.getCountries().subscribe(
      (data) => {
        this.paeseToUpdate = data.find((item) => item.nome === this.selectedCountry);
        this.humanRightsBenchmark = this.paeseToUpdate.benchmark[0].gravita;
        this.lgbtRightsBenchmark = this.paeseToUpdate.benchmark[1].gravita;
        this.disabilityRightsBenchmark = this.paeseToUpdate.benchmark[2].gravita;
        this.racismRightsBenchmark = this.paeseToUpdate.benchmark[3].gravita;
        this.womenRightsBenchmark = this.paeseToUpdate.benchmark[4].gravita;
        this.humanRightsDescription = this.paeseToUpdate.benchmark[0].descrizione;
      },
      (error) => {
        console.log("Errore nel recupero dei dati: ", error);
      },
    )
  }

  salvaModifiche() {
    this.paeseToUpdate.benchmark[0].gravita = this.humanRightsBenchmark;
    this.paeseToUpdate.benchmark[1].gravita = this.lgbtRightsBenchmark;
    this.paeseToUpdate.benchmark[2].gravita = this.disabilityRightsBenchmark;
    this.paeseToUpdate.benchmark[3].gravita = this.racismRightsBenchmark;
    this.paeseToUpdate.benchmark[4].gravita = this.womenRightsBenchmark;
    this.paeseToUpdate.benchmark[0].descrizione = this.humanRightsDescription;
    this.countryService.updateBenchmarkByName(this.paeseToUpdate.nome,  this.paeseToUpdate.benchmark).subscribe({
      next: (response) => {
        alert("Modifica avvenuta con successo!");
        this.resetPage();
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  resetPage() {
    this.paeseToUpdate = null;
    this.humanRightsBenchmark = 0;
    this.lgbtRightsBenchmark = 0;
    this.disabilityRightsBenchmark = 0;
    this.racismRightsBenchmark = 0;
    this.womenRightsBenchmark = 0;
    this.humanRightsDescription = '';
    this.selectedCountry = '';
    this.searchTerm = '';
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
  const clickedInside = event.target instanceof HTMLElement && event.target.closest('ul');
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
