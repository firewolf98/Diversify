import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { FormsModule } from '@angular/forms'; // Importa FormsModule per [(ngModel)]

// Elenco dei Paesi dell'Unione Europea
const countriesDB = [
  'Tutti', 'Austria', 'Belgio', 'Bulgaria', 'Cipro', 'Croazia', 'Danimarca', 'Estonia', 'Finlandia', 'Francia', 
  'Germania', 'Grecia', 'Irlanda', 'Italia', 'Lettonia', 'Lituania', 'Lussemburgo', 'Malta', 'Paesi Bassi', 
  'Polonia', 'Portogallo', 'Repubblica Ceca', 'Romania', 'Slovacchia', 'Slovenia', 'Spagna', 'Svezia'
];

interface Campaign {
  title: string;
  country: string;
  status: 'Pubblicata' | 'Terminata';
  category?: string;
  description?: string;
  targetFunds?: number;
  currentFunds?: number;
  deadline?: string;
  donationLink?: string;
  backgroundImage?: string; // Percorso immagine (opzionale)
}

@Component({
  selector: 'app-gestione-campagne',
  standalone: true,
  templateUrl: './gestione-campagne.component.html',
  styleUrls: ['./gestione-campagne.component.css'],
  imports: [CommonModule, FormsModule], // Aggiungi CommonModule e FormsModule
})
export class GestioneCampagneComponent {
  campaigns: Campaign[] = [];
  europeanUnionCountries: string[] = [];
  selectedStatus: string = '';
  filteredCampaigns: Campaign[] = [];
  activeComponent: string = ''; // Variabile per gestire il componente attivo
  campaign: Campaign = this.initializeCampaign();
  imagePreview: string | undefined;
  countries = countriesDB;
  selectedCountry = '';  // Paese 
  searchTerm: string = '';
  filteredCountries: string[] = [];
  isDropdownVisible: boolean = false;

  ngOnInit(): void {
    this.filteredCountries = this.countries;
    this.loadCampaigns();
    this.loadCountries(); // Carica i paesi
    this.filterAndSortCampaigns();
  }

  // Inizializza una campagna vuota
  initializeCampaign(): Campaign {
    return {
      title: '',
      country: '',
      status: 'Pubblicata',
      category: '',
      description: '',
      targetFunds: undefined,
      currentFunds: 0,
      deadline: '',
      donationLink: '',
      backgroundImage: '',
    };
  }

  // Funzione per caricare le campagne
  loadCampaigns(): void {
    this.campaigns = [
      {
        title: 'Progetto 0',
        country: 'Italia',
        status: 'Pubblicata',
        category: 'Sostenibilità',
        description: 'Promuoviamo la sostenibilità ambientale.',
        targetFunds: 10000,
        currentFunds: 4000,
        deadline: '2024-12-31',
      },
      {
        title: 'Progetto 1',
        country: 'Francia',
        status: 'Pubblicata',
        category: 'Sostenibilità',
        description: 'Promuoviamo la sostenibilità ambientale.',
        targetFunds: 10000,
        currentFunds: 4000,
        deadline: '2024-12-31',
      },
      {
        title: 'Progetto 2',
        country: 'Italia',
        status: 'Pubblicata',
        category: 'Sostenibilità',
        description: 'Promuoviamo la sostenibilità ambientale.',
        targetFunds: 10000,
        currentFunds: 4000,
        deadline: '2024-12-31',
      },
      {
        title: 'Progetto 3',
        country: 'Spagna',
        status: 'Pubblicata',
        category: 'Sostenibilità',
        description: 'Promuoviamo la sostenibilità ambientale.',
        targetFunds: 10000,
        currentFunds: 4000,
        deadline: '2024-12-31',
      },
      {
        title: 'Progetto 4',
        country: 'Germania',
        status: 'Pubblicata',
        category: 'Sostenibilità',
        description: 'Promuoviamo la sostenibilità ambientale.',
        targetFunds: 10000,
        currentFunds: 4000,
        deadline: '2024-12-31',
      },
      {
        title: 'Progetto 5',
        country: 'Polonia',
        status: 'Pubblicata',
        category: 'Sostenibilità',
        description: 'Promuoviamo la sostenibilità ambientale.',
        targetFunds: 10000,
        currentFunds: 4000,
        deadline: '2024-12-31',
      },
      {
        title: 'Progetto 6',
        country: 'Italia',
        status: 'Pubblicata',
        category: 'Sostenibilità',
        description: 'Promuoviamo la sostenibilità ambientale.',
        targetFunds: 10000,
        currentFunds: 4000,
        deadline: '2024-12-31',
      }
      
    ];
  }

  // Funzione per caricare i paesi appartenenti all'Unione Europea
  loadCountries(): void {
    this.europeanUnionCountries = [
      'Tutti', 'Austria', 'Belgio', 'Bulgaria', 'Croazia', 'Cipro', 'Repubblica Ceca',
      'Danimarca', 'Estonia', 'Finlandia', 'Francia', 'Germania', 'Grecia',
      'Ungheria', 'Irlanda', 'Italia', 'Lettonia', 'Lituania', 'Lussemburgo',
      'Malta', 'Paesi Bassi', 'Polonia', 'Portogallo', 'Romania', 'Slovacchia',
      'Slovenia', 'Spagna', 'Svezia',
    ];
  }

  // Funzione per filtrare e ordinare le campagne
  filterAndSortCampaigns(): void {
    this.filteredCampaigns = this.campaigns
      .filter(
        campaign =>
          (!this.selectedCountry || campaign.country === this.selectedCountry) &&
          (!this.selectedStatus || campaign.status === this.selectedStatus),
      )
      .sort((a, b) => a.title.localeCompare(b.title)); // Ordina le campagne alfabeticamente per titolo
  }

  // Getter per i paesi unici
  get uniqueCountries(): string[] {
    return this.europeanUnionCountries;
  }

  // Funzione per rimuovere una campagna
  removeCampaign(title: string): void {
    // Rimuovi la campagna dalla lista
    this.campaigns = this.campaigns.filter(campaign => campaign.title !== title);
    this.filterAndSortCampaigns();
  }

  // Funzione per modificare una campagna
  editCampaign(campaign: Campaign): void {
    this.setActiveComponent('editCampagna', campaign);
  }

  resetFilters(): void {
    this.selectedCountry = ''; // Resetta il filtro del paese
    this.selectedStatus = ''; // Resetta il filtro dello stato
    this.filterAndSortCampaigns(); // Applica i filtri aggiornati
}

  // Funzione per attivare un componente specifico (creazione o modifica)
  setActiveComponent(component: string, selectedCampaign?: Campaign): void {
    this.activeComponent = component;

    if (component === 'editCampagna' && selectedCampaign) {
        console.log('Campagna selezionata:', selectedCampaign); // Debug
        this.campaign = { ...selectedCampaign }; // Clona i dati della campagna selezionata
        this.selectedCountry = this.campaign.country; // Imposta il paese selezionato
        console.log('Campagna nel form:', this.campaign); // Debug
    } else {
        this.resetCampaign(); // Azzera il modulo se si sta creando una nuova campagna
    }
}

  // Funzione per azzerare i dati della campagna
  resetCampaign(): void {
    this.campaign = this.initializeCampaign();
    this.imagePreview = undefined;
  }

  // Funzione per salvare una campagna
  saveCampaign(): void {
    if (
      !this.campaign.title ||
      !this.campaign.country ||
      !this.campaign.category ||
      !this.campaign.targetFunds ||
      !this.campaign.deadline
    ) {
      return; // Verifica che i campi obbligatori siano compilati
    }

    if (this.activeComponent === 'createCampagna') {
      this.campaigns.push({ ...this.campaign });
    } else if (this.activeComponent === 'editCampagna') {
      const index = this.campaigns.findIndex(c => c.title === this.campaign.title);
      if (index !== -1) {
        this.campaigns[index] = { ...this.campaign };
      }
    }

    this.filterAndSortCampaigns();
    this.setActiveComponent('');
  }

  // Gestione immagine selezionata
  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result as string; // Anteprima dell'immagine
        this.campaign.backgroundImage = this.imagePreview;
      };
      reader.readAsDataURL(file);
    }
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
  goBack(): void {
    this.setActiveComponent('gestioneCampagne');
  }
  
}
