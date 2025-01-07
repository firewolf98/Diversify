import { Component, OnInit, HostListener, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
import { SearchingCountryService } from '../services/searching-country.service'; // importa il servizio
 
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
  menuVisible: boolean = false;
  isLogged: boolean = false;
  utente: any;
  token: string | null = null;
  ruoloUtente: boolean = false;
 
  constructor(
    private router: Router,
    private authService: AuthService,
    private userService:UserService,
    private searchingCountryService: SearchingCountryService 
  ) {}
 
  ngOnInit(): void {
    this.filteredCountries = countriesDB;
  
    // Verifica lo stato di login
    this.authService.isLoggedIn().subscribe((status: boolean) => {
      this.isLogged = status;
      if (status) {
        this.token = localStorage.getItem('auth_token'); // Recupera il token se l'utente è loggato
        if (this.token) {
          // Aggiorna i dati dell'utente
          this.userService.getUserFromToken(this.token).subscribe(
            (data) => {
              this.utente = data;
              this.ruoloUtente = data.ruolo; // Aggiorna il ruolo
            },
            (error) => {
              console.error('Errore nel caricamento dei dati utente:', error);
            }
          );
        }
      }
    });
  }
  
 
  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const searchValue = input.value.trim().toLowerCase();
 
    this.isDropdownVisible = searchValue.length > 0;
 
    if (searchValue.length === 0) {
      this.filteredCountries = [];
      this.searchingCountryService.resetMap();
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
    this.searchingCountryService.selectCountry(country);
  }
 
  toggleMenu(): void {
    this.menuVisible = !this.menuVisible;
  }
 
  closeMenu(): void {
    this.menuVisible = false; // Chiude il menu
  }
 
  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    const clickedInside = event.target instanceof HTMLElement && event.target.closest('.menu-container');
    const clickedMenuIcon = event.target instanceof HTMLElement && event.target.closest('.menu-icon-container');
    const clickedSearch = event.target instanceof HTMLElement && event.target.closest('.search-container');
   
    if (!clickedInside && !clickedMenuIcon && !clickedSearch) {
      this.menuVisible = false;  // Chiudi il menu se clicchi fuori
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
    this.closeMenu();
  }
 
  navigateToProfile(): void {
    console.log("RUOLO",this.ruoloUtente);
    if (this.ruoloUtente) {
      this.router.navigate(['/generale-amministratore'] );
      this.closeMenu();
    } else {
      this.router.navigate(['/scheda-area-personale']);
      this.closeMenu();
    }
  }
 
  logout(): void {
    // Chiama il metodo logout del servizio AuthService
    this.authService.logout();
    this.closeMenu();
    this.router.navigate(['/']);
  }
}