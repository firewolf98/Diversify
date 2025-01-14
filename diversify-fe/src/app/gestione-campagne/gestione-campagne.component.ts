import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { FormsModule } from '@angular/forms'; // Importa FormsModule per [(ngModel)]

// Elenco dei Paesi dell'Unione Europea
const countriesDB = [
  'Tutti', 'Austria', 'Belgio', 'Bulgaria', 'Cipro', 'Croazia', 'Danimarca', 'Estonia', 'Finlandia', 'Francia',
  'Germania', 'Grecia', 'Irlanda', 'Italia', 'Lettonia', 'Lituania', 'Lussemburgo', 'Malta', 'Paesi Bassi',
  'Polonia', 'Portogallo', 'Repubblica Ceca', 'Romania', 'Slovacchia', 'Slovenia', 'Spagna', 'Svezia',
];

interface Campaign {
  id: string;
  title: string;
  country: string;
  status: 'attiva' | 'chiusa';
  category?: string;
  description?: string;
  targetFunds?: number;
  currentFunds?: number;
  deadline?: string;
  donationLink?: string;
  backgroundImage?: string; // Link immagine (opzionale)
}

@Component({
  selector: 'app-gestione-campagne',
  standalone: true,
  templateUrl: './gestione-campagne.component.html',
  styleUrls: ['./gestione-campagne.component.css'],
  imports: [CommonModule, FormsModule],
})
export class GestioneCampagneComponent {
  campaigns: Campaign[] = [];
  selectedStatus: string = '';
  filteredCampaigns: Campaign[] = [];
  activeComponent: string = ''; // Variabile per gestire il componente attivo
  campaign: Campaign = this.initializeCampaign();
  countries = countriesDB;
  selectedCountry = ''; // Paese
  searchTerm: string = '';
  filteredCountries: string[] = [];
  isDropdownVisible: boolean = false;

  ngOnInit(): void {
    this.filteredCountries = this.countries;
    this.loadCampaigns();
    this.filterAndSortCampaigns();
  }

  // Inizializza una campagna vuota
  initializeCampaign(): Campaign {
    return {
      id: crypto.randomUUID(),
      title: '',
      country: '',
      status: 'attiva',
      category: '',
      description: '',
      targetFunds: undefined,
      currentFunds: 0,
      deadline: '',
      donationLink: '',
      backgroundImage: '',
    };
  }

  // Getter per i paesi unici
  get uniqueCountries(): string[] {
    return this.countries;
  }

  // Carica le campagne di esempio
  loadCampaigns(): void {
    this.campaigns = [
      {
        id: '0',
        title: 'Progetto 0',
        country: 'Italia',
        status: 'attiva',
        category: 'Sostenibilità',
        description: 'Promuoviamo la sostenibilità ambientale.',
        targetFunds: 10000,
        currentFunds: 4000,
        deadline: '2024-12-31',
        backgroundImage: 'https://example.com/image0.jpg',
      },
      // Altri esempi...
    ];
  }

  // Filtra e ordina le campagne
  filterAndSortCampaigns(): void {
    this.filteredCampaigns = this.campaigns
      .filter(
        (campaign) =>
          (!this.selectedCountry || campaign.country === this.selectedCountry) &&
          (!this.selectedStatus || campaign.status === this.selectedStatus)
      )
      .sort((a, b) => a.title.localeCompare(b.title));
  }

  // Rimuove una campagna
  removeCampaign(id: string): void {
    this.campaigns = this.campaigns.filter((campaign) => campaign.id !== id);
    this.filterAndSortCampaigns();
  }

  // Modifica una campagna
  editCampaign(campaign: Campaign): void {
    this.setActiveComponent('editCampagna', campaign);
  }

  resetFilters(): void {
    this.selectedCountry = '';
    this.selectedStatus = '';
    this.filterAndSortCampaigns();
  }

  // Gestisce il componente attivo
  setActiveComponent(component: string, selectedCampaign?: Campaign): void {
    this.activeComponent = component;

    if (component === 'editCampagna' && selectedCampaign) {
      this.campaign = { ...selectedCampaign };
    } else {
      this.resetCampaign();
    }
  }

  // Resetta i dati della campagna
  resetCampaign(): void {
    this.campaign = this.initializeCampaign();
  }

  // Salva una campagna
  saveCampaign(): void {
    if (
      !this.campaign.title ||
      !this.campaign.country ||
      !this.campaign.category ||
      !this.campaign.targetFunds ||
      !this.campaign.deadline ||
      !this.campaign.backgroundImage
    ) {
      alert('Compila tutti i campi obbligatori!');
      return;
    }

    if (this.activeComponent === 'createCampagna') {
      this.campaigns.push({ ...this.campaign });
    } else if (this.activeComponent === 'editCampagna') {
      const index = this.campaigns.findIndex((c) => c.id === this.campaign.id);
      if (index !== -1) {
        this.campaigns[index] = { ...this.campaign };
      } else {
        console.error('Campagna non trovata per la modifica:', this.campaign);
      }
    }

    this.filterAndSortCampaigns();
    this.setActiveComponent('');
  }

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const searchValue = input.value.trim().toLowerCase();

    this.isDropdownVisible = searchValue.length > 0;

    if (searchValue.length === 0) {
      this.filteredCountries = [];
      return;
    }

    this.filteredCountries = this.countries
      .filter((country) => country.toLowerCase().includes(searchValue))
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

  goBack(): void {
    this.setActiveComponent('gestioneCampagne');
  }
}
