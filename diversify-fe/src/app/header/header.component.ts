import { Component, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

const countriesDB = [
  'Austria', 'Belgio', 'Bulgaria', 'Cipro', 'Croazia', 'Danimarca', 'Estonia', 'Finlandia', 'Francia',
  'Germania', 'Grecia', 'Irlanda', 'Italia', 'Lettonia', 'Lituania', 'Lussemburgo', 'Malta', 'Paesi Bassi', 
  'Polonia', 'Portogallo', 'Repubblica Ceca', 'Romania', 'Slovacchia', 'Slovenia', 'Spagna', 'Svezia'
];

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  imports: [CommonModule, FormsModule],
  standalone: true
})
export class HeaderComponent implements OnInit {
  searchTerm: string = '';
  filteredCountries: string[] = [];
  isDropdownVisible: boolean = false;
  menuVisible: boolean = false;  // variabile per gestire la visibilità del menu
  isLogged: boolean = false; // Variabile locale per il login stato

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    this.filteredCountries = countriesDB;
    this.authService.isLoggedIn().subscribe((status: boolean) => {
      this.isLogged = status; // Aggiorna la variabile locale
    });
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

isLoggedIn(): boolean {
  return this.isLogged;
}

isHomePage(): boolean {
  return this.router.url === '/';
}

navigateTo(route: string): void {
  this.router.navigate([route]);
}

navigateToProfile(): void {
  if (this.authService.isAdmin()) {
    this.router.navigate(['/pagina-profilo-amministratore']);
  } else {
    this.router.navigate(['/scheda-area-personale']);
  }
}
}
