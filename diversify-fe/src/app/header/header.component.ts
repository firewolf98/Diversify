import { Component, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

const countriesDB = [
  'Austria', 'Belgio', 'Bulgaria', 'Cipro', 'Croazia', 'Danimarca', 'Estonia', 'Finlandia', 'Francia',
  'Germania', 'Grecia', 'Irlanda', 'Italia', 'Lettonia', 'Lituania', 'Lussemburgo', 'Malta', 'Paesi Bassi', 
  'Polonia', 'Portogallo', 'Repubblica Ceca', 'Romania', 'Slovacchia', 'Slovenia', 'Spagna', 'Svezia'
];

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  imports: [CommonModule, FormsModule]
})
export class HeaderComponent implements OnInit {
  searchTerm: string = '';
  filteredCountries: string[] = [];
  isDropdownVisible: boolean = false;
  menuVisible: boolean = false;  // Nuova variabile per gestire la visibilità del menu

  ngOnInit(): void {
    this.filteredCountries = countriesDB;
  }

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const searchValue = input.value.trim().toLowerCase();

    this.isDropdownVisible = searchValue.length > 0;

    if (searchValue.length === 0) {
      this.filteredCountries = [];
      return;
    }

    this.filteredCountries = countriesDB
      .filter(country => country.toLowerCase().includes(searchValue))
      .sort((a, b) => a.toLowerCase().indexOf(searchValue) - b.toLowerCase().indexOf(searchValue));
  }

  toggleDropdown(): void {
    this.isDropdownVisible = !this.isDropdownVisible;
  }

  selectCountry(country: string): void {
    this.searchTerm = country;
    this.filteredCountries = [];
    this.isDropdownVisible = false;
  }

  toggleMenu(): void {
    this.menuVisible = !this.menuVisible;  // Toggle visibilità menu
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    const clickedInside = event.target instanceof HTMLElement && event.target.closest('.menu-container');
    const clickedMenuIcon = event.target instanceof HTMLElement && event.target.closest('.menu-icon-container');
    if (!clickedInside && !clickedMenuIcon) {
      this.menuVisible = false; // Chiudi il menu se clicchi fuori
    }
}

}
