import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { FormsModule } from '@angular/forms'; // Importa FormsModule per [(ngModel)]

interface Campaign {
  title: string;
  country: string;
  status: 'Pubblicata' | 'Terminata';
  donationLink?: string;  // Add this line to include the donationLink property
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
  selectedCountry: string = '';
  selectedStatus: string = '';
  filteredCampaigns: Campaign[] = [];
  activeComponent: string = ''; // Variabile per gestire il componente attivo
  campaign: Campaign = { title: '', country: '', status: 'Pubblicata' };
  imagePreview: string | undefined;

  ngOnInit(): void {
    this.loadCampaigns();
    this.loadCountries(); // Carica i paesi
    this.filterAndSortCampaigns();
  }

  // Funzione per caricare le campagne
  loadCampaigns(): void {
    this.campaigns = [
      { title: 'Progetto Eco', country: 'Italia', status: 'Pubblicata' },
      { title: 'Sostenibilità Ambientale', country: 'Francia', status: 'Terminata' },
      { title: 'Innovazione Verde', country: 'Germania', status: 'Pubblicata' },
      { title: 'Campagna Solidale', country: 'Spagna', status: 'Terminata' },
      { title: 'Energie Rinnovabili', country: 'Italia', status: 'Pubblicata' },
      { title: 'Riciclo e Recupero', country: 'Portogallo', status: 'Pubblicata' },
      { title: 'Tutela del Territorio', country: 'Svezia', status: 'Terminata' },
      { title: 'Zero Emissioni', country: 'Danemarca', status: 'Pubblicata' },
      { title: 'Educazione Ambientale', country: 'Norvegia', status: 'Terminata' },
      { title: 'Raccolta Differenziata', country: 'Finlandia', status: 'Pubblicata' },
    ];
  }

  // Funzione per caricare i paesi appartenenti all'Unione Europea
  loadCountries(): void {
    this.europeanUnionCountries = [
      'Austria', 'Belgio', 'Bulgaria', 'Croazia', 'Cipro', 'Repubblica Ceca',
      'Danimarca', 'Estonia', 'Finlandia', 'Francia', 'Germania', 'Grecia',
      'Ungheria', 'Irlanda', 'Italia', 'Lettonia', 'Lituania', 'Lussemburgo',
      'Malta', 'Paesi Bassi', 'Polonia', 'Portogallo', 'Romania', 'Slovacchia',
      'Slovenia', 'Spagna', 'Svezia'
    ];
  }

  // Funzione per filtrare e ordinare le campagne
  filterAndSortCampaigns(): void {
    this.filteredCampaigns = this.campaigns
      .filter(
        campaign =>
          (!this.selectedCountry || campaign.country === this.selectedCountry) &&
          (!this.selectedStatus || campaign.status === this.selectedStatus)
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
  editCampaign(campaignTitle: string): void {
    const campaignToEdit = this.campaigns.find(campaign => campaign.title === campaignTitle);
    if (campaignToEdit) {
      this.campaign = { ...campaignToEdit }; // Popola i dati della campagna da modificare
    }
    this.setActiveComponent('editCampagna');
  }

  // Funzione per attivare un componente specifico (creazione o modifica)
  setActiveComponent(component: string, campaign?: Campaign): void {
    this.activeComponent = component;

    // Se la campagna è passata per la modifica, la imposta come campagna da modificare
    if (campaign) {
      this.campaign = { ...campaign };
    } else {
      this.resetCampaign(); // Azzera il modulo se si sta creando una nuova campagna
    }
  }

  // Funzione per azzerare i dati della campagna
  resetCampaign(): void {
    this.campaign = { title: '', country: '', status: 'Pubblicata' };
  }

  // Funzione per salvare una campagna
  saveCampaign(): void {
    if (!this.campaign.title || !this.campaign.country || !this.campaign.status) {
      return; // Verifica che tutti i campi siano compilati
    }

    if (this.activeComponent === 'createCampagna') {
      // Aggiungi una nuova campagna
      this.campaigns.push(this.campaign);
    } else if (this.activeComponent === 'editCampagna') {
      // Modifica una campagna esistente
      const index = this.campaigns.findIndex(c => c.title === this.campaign.title);
      if (index !== -1) {
        this.campaigns[index] = { ...this.campaign };
      }
    }

    this.filterAndSortCampaigns();
    this.setActiveComponent('');
  }

  onImageSelected(event: any): void {
    const file = event.target.files[0];  // Get the selected file
    if (file) {
      console.log('Selected Image:', file);
      // Add logic to process the selected image (e.g., store it in a variable, upload it, etc.)
    }
  }
}
