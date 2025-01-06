import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { UserService } from './user.service';
 
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/utenti';
  private readonly TOKEN_KEY = 'auth_token';
  private isLoggedInSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(this.hasToken());
  private userSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null); // Nuovo subject per i dati dell'utente
 
  constructor(private http: HttpClient, private userService: UserService) {}
 
  register(user: { name:string; lastName:string; username: string; password: string; email: string; codiceFiscale: string; domanda: string; risposta: string }): Observable<any> {
    // Crea l'oggetto per la richiesta di registrazione
    const registerRequest = {
      name: user.name,
      lastName: user.lastName,
      username: user.username,
      password: user.password,
      email: user.email,
      codiceFiscale: user.codiceFiscale,
      domanda: user.domanda,
      risposta: user.risposta
     
    };
 
    return this.http.post(`${this.apiUrl}/registrazione`, registerRequest);
  }
 
  login(user: { email: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, user).pipe(
      tap((response) => {
        if (response && response.token) {
          this.saveToken(response.token);
          console.log("LOGIN", response.token);
          this.userService.setToken(response.token);
          this.isLoggedInSubject.next(true);
        }
      })
    );
  }
 
  private saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }
 
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }
 
  private hasToken(): boolean {
    return !!this.getToken();
  }
 
  isLoggedIn(): Observable<boolean> {
    return this.isLoggedInSubject.asObservable();
  }
 
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.isLoggedInSubject.next(false);
    this.userSubject.next(null);
    this.userService.setToken(null);
  }
 
 
  // Nuovo metodo per recuperare i dati dell'utente
  private getUserFromToken(): Observable<any> {
    const token = this.getToken();
    if (!token) {
      return new Observable<any>((observer) => {
        observer.next(null); // Se non c'è token, ritorna null
        observer.complete();
      });
    }
   
    return this.http.get<any>(`${this.apiUrl}/recupera_utente`, {
      headers: { Authorization: `Bearer ${token}` }
    }).pipe(
      tap((user) => {
        if (user) {
          this.userSubject.next(user); // Salva i dati dell'utente
        }
      })
    );
  }
 
  // Metodo per verificare se l'utente è un admin
  isAdmin(): boolean {
    this.getUserFromToken();
    const user = this.userSubject.value;
    return user && user.ruolo;
  }
 
  // Metodo per ottenere i dati dell'utente
  getUser(): Observable<any> {
    return this.userSubject.asObservable();
  }
 
}