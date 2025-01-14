import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CampagnaService } from '../services/campagna-crowdfunding.service';

// Interfaccia per la campagna
interface Campaign {
  id: string;
  title: string;
  country: string;
  status: 'attiva' | 'chiusa';
  category: string;
  description: string;
  targetFunds: number;
  currentFunds: number;
  deadline: string;
  donationLink: string;
  backgroundImage: string;
}

@Component({
  selector: 'app-gestione-campagne',
  standalone: true,
  templateUrl: './gestione-campagne.component.html',
  styleUrls: ['./gestione-campagne.component.css'],
  imports: [CommonModule, FormsModule],
})
export class GestioneCampagneComponent implements OnInit {
  campaigns: Campaign[] = [];
  filteredCampaigns: Campaign[] = [];
  selectedCountry: string = ''; // Filtro per Paese
  selectedStatus: string = ''; // Filtro per Stato
  activeComponent: string = ''; // Stato del componente attivo
  campaign: Campaign = this.initializeCampaign(); // Campagna inizializzata sempre valida
  countries: string[] = [
    'Tutti', 'Austria', 'Belgio', 'Bulgaria', 'Cipro', 'Croazia', 'Danimarca',
    'Estonia', 'Finlandia', 'Francia', 'Germania', 'Grecia', 'Irlanda', 'Italia',
    'Lettonia', 'Lituania', 'Lussemburgo', 'Malta', 'Paesi Bassi', 'Polonia',
    'Portogallo', 'Repubblica Ceca', 'Romania', 'Slovacchia', 'Slovenia',
    'Spagna', 'Svezia',
  ];
  isDropdownVisible: boolean = false;
  searchTerm: string = '';
  filteredCountries: string[] = [];
  imagePreview: string | undefined;

  constructor(private campagnaService: CampagnaService) {}

  ngOnInit(): void {
    this.loadCampaigns();
    this.filteredCountries = this.countries;
  }

  // Carica le campagne dal backend
  loadCampaigns(): void {
    this.campagnaService.getAllCampagne().subscribe(
      (data) => {
        console.log('Dati ricevuti dal backend:', data);
        this.campaigns = data.map((c) => this.mapCampaign(c)); // Mappatura dei dati ricevuti
        this.filterAndSortCampaigns();
      },
      (error) => {
        console.error('Errore durante il caricamento delle campagne:', error);
      }
    );
  }

  // Mappa i dati dal backend all'interfaccia interna
  private mapCampaign(c: any): Campaign {
    return {
      id: c.idCampagna,
      title: c.titolo,
      country: c.paese,
      status: c.stato,
      category: c.categoria,
      description: c.descrizione,
      targetFunds: c.sommaDaRaccogliere,
      currentFunds: c.sommaRaccolta,
      deadline: c.dataPrevistaFine,
      donationLink: c.linkDonazione,
      backgroundImage: c.immagine,
    };
  }

  // Filtra e ordina le campagne
  filterAndSortCampaigns(): void {
    console.log('Campagne prima di essere filtrate:', this.campaigns);
    console.log('Paese selezionato:', this.selectedCountry);
    console.log('Stato selezionato:', this.selectedStatus);

    // Filtra le campagne in base ai criteri selezionati
    this.filteredCampaigns = this.campaigns
      .filter((campaign) => {
        const matchesCountry = !this.selectedCountry || this.selectedCountry === 'Tutti' || campaign.country === this.selectedCountry;
        const matchesStatus = !this.selectedStatus || campaign.status === this.selectedStatus;
        return matchesCountry && matchesStatus;
      })
      .sort((a, b) => a.title.localeCompare(b.title));

    console.log('Campagne filtrate:', this.filteredCampaigns);
}

  // Resetta i filtri
  resetFilters(): void {
    this.selectedCountry = '';
    this.selectedStatus = '';
    this.filterAndSortCampaigns();
  }

  // Gestisce il componente attivo
  setActiveComponent(component: string, selectedCampaign?: Campaign): void {
    this.activeComponent = component;
    this.campaign = selectedCampaign ? { ...selectedCampaign } : this.initializeCampaign();
  }

  // Inizializza una nuova campagna vuota
  initializeCampaign(): Campaign {
    return {
      id: crypto.randomUUID(),
      title: '',
      country: '',
      status: 'attiva',
      category: '',
      description: '',
      targetFunds: 0,
      currentFunds: 0,
      deadline: '',
      donationLink: '',
      backgroundImage: '',
    };
  }

  // Salva una campagna
  saveCampaign(): void {
    if (!this.campaign) return;

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

    const isNew = !this.campaign.id;
    const payload = this.mapToBackend(this.campaign);

    const request = isNew
      ? this.campagnaService.createCampagna(payload)
      : this.campagnaService.updateCampagna(payload);

    request.subscribe(
      () => {
        alert(isNew ? 'Campagna creata con successo!' : 'Campagna aggiornata con successo!');
        this.loadCampaigns();
        this.setActiveComponent('');
      },
      (error) => {
        console.error('Errore durante il salvataggio della campagna:', error);
      }
    );
  }

  // Mappa i dati al formato richiesto dal backend
  private mapToBackend(campaign: Campaign): any {
    return {
      idCampagna: campaign.id,
      titolo: campaign.title,
      paese: campaign.country,
      stato: campaign.status,
      categoria: campaign.category,
      descrizione: campaign.description,
      sommaDaRaccogliere: campaign.targetFunds,
      sommaRaccolta: campaign.currentFunds,
      dataPrevistaFine: campaign.deadline,
      linkDonazione: campaign.donationLink,
      immagine: campaign.backgroundImage,
    };
  }

  // Rimuove una campagna
  removeCampaign(id: string): void {
    if (confirm('Sei sicuro di voler eliminare questa campagna?')) {
      this.campagnaService.deleteCampagna(id).subscribe(
        () => {
          alert('Campagna eliminata con successo!');
          this.loadCampaigns();
        },
        (error) => {
          console.error('Errore durante l\'eliminazione della campagna:', error);
        }
      );
    }
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

  toggleDropdown(): void {
    this.isDropdownVisible = !this.isDropdownVisible;
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

  onCountrySelected(country: string): void {
    this.selectedCountry = country;
    this.isDropdownVisible = false;
    this.filterAndSortCampaigns(); // Applica i filtri aggiornati
  }

  goBack(): void {
    this.setActiveComponent('gestioneCampagne');
  }
}
