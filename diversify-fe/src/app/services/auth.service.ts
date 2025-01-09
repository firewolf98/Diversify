import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, of, tap } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/utenti';
  private apiPostUrl = 'http://localhost:8080/posts';
  private readonly TOKEN_KEY = 'auth_token';
  private isLoggedInSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(this.hasToken());
  private userSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient, private userService: UserService) {}

  // Metodo di registrazione
  register(user: { name: string; lastName: string; username: string; password: string; email: string; codiceFiscale: string; domanda: string; risposta: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/registrazione`, user);
  }

  // Metodo di login
  login(user: { email: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, user).pipe(
      tap((response) => {
        if (response && response.token) {
          this.saveToken(response.token);
          this.userService.setToken(response.token);
          this.isLoggedInSubject.next(true);

          // Recupera i dati dell'utente e aggiorna il BehaviorSubject
          this.getUserFromToken().subscribe();
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

  
  private getUserFromToken(): Observable<any> {
    const token = this.getToken();
    if (!token) {
      return of(null);
    }
  
    return this.http.get<any>(`${this.apiUrl}/recupera_utente`, {
      headers: { Authorization: `Bearer ${token}` },
    }).pipe(
      tap((user) => {
        if (user) {
          this.userSubject.next(user);
          console.log('Dati utente caricati:', user);
        }
      }),
      catchError((error) => {
        console.error('Errore durante il recupero dei dati utente:', error);
        return of(null);
      })
    );
  }
  

  // Metodo per ottenere l'utente corrente
  getUser(): Observable<any> {
    const token = this.getToken();
    if (!token) {
      return of(null);
    }

    const user = this.userSubject.value;
    if (user) {
      return of(user);
    }

    // Se il token esiste ma l'utente non è in memoria, recuperalo dal backend
    return this.getUserFromToken().pipe(
      tap((userData) => {
        if (userData) {
          this.userSubject.next(userData);
        }
      }),
      catchError(() => of(null)) // Restituisci null in caso di errore
    );
  }

  getLoggedUsername(): string | null {
    const user = this.userSubject.value;
    return user ? user.username : null;
  }

  // Metodo per verificare se l'utente è bannato
  isUserBanned(): boolean {
    const user = this.userSubject.value;
    return user ? user.banned : false;
  }

  savePost(postData: { titolo: string; contenuto: string; categoria: string; userAutore: string }): Observable<any> {
    return this.http.post(this.apiPostUrl, postData, {
      headers: { Authorization: `Bearer ${this.getToken()}` },
    });
  }
}
