import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { FormsModule } from '@angular/forms'; // Importa FormsModule per [(ngModel)]

interface Campaign {
  title: string;
  country: string;
  status: 'Pubblicata' | 'Terminata';
}

@Component({
  selector: 'app-gestione-campagne',
  standalone: true,
  templateUrl: './gestione-campagne.component.html',
  styleUrls: ['./gestione-campagne.component.css'],
  imports: [CommonModule, FormsModule], // Aggiungi CommonModule e FormsModule
})
export class GestioneCampagneComponent {
  campaigns: Campaign[] = [
    { title: 'Progetto Eco', country: 'Italia', status: 'Pubblicata' },
    { title: 'Sostenibilità Ambientale', country: 'Francia', status: 'Terminata' },
    { title: 'Innovazione Verde', country: 'Germania', status: 'Pubblicata' },
    { title: 'Campagna Solidale', country: 'Spagna', status: 'Terminata' },
  ];

  // Lista fissa dei paesi appartenenti all'Unione Europea
  europeanUnionCountries: string[] = [
    'Austria', 'Belgio', 'Bulgaria', 'Croazia', 'Cipro', 'Repubblica Ceca', 
    'Danimarca', 'Estonia', 'Finlandia', 'Francia', 'Germania', 'Grecia', 
    'Ungheria', 'Irlanda', 'Italia', 'Lettonia', 'Lituania', 'Lussemburgo', 
    'Malta', 'Paesi Bassi', 'Polonia', 'Portogallo', 'Romania', 'Slovacchia', 
    'Slovenia', 'Spagna', 'Svezia'
  ];

  selectedCountry: string = '';
  selectedStatus: string = '';
  filteredCampaigns: Campaign[] = [];

  ngOnInit(): void {
    this.filterAndSortCampaigns();
  }

  filterAndSortCampaigns(): void {
    this.filteredCampaigns = this.campaigns
      .filter(
        campaign =>
          (!this.selectedCountry || campaign.country === this.selectedCountry) &&
          (!this.selectedStatus || campaign.status === this.selectedStatus)
      )
      .sort((a, b) => a.title.localeCompare(b.title));
  }

  removeCampaign(title: string): void {
    this.campaigns = this.campaigns.filter(campaign => campaign.title !== title);
    this.filterAndSortCampaigns();
  }

  createCampaign(): void {
    alert('Funzionalità di creazione non ancora implementata!');
  }

  editCampaign(title: string): void {
    alert(`Modifica della campagna "${title}" non ancora implementata!`);
  }

  get uniqueCountries(): string[] {
    // Restituisce solo i paesi della lista fissa dei paesi UE
    return this.europeanUnionCountries;
  }
}
