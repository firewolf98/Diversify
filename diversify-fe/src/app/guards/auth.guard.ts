import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {
    return this.authService.isLoggedIn().pipe(
      map((isLoggedIn) => {
        if (isLoggedIn) {
          console.log("ACCESS DENIED: Utente già loggato");
          this.router.navigate(['/']); // Reindirizza alla homepage se loggato
          return false;
        }
        return true; // Accesso consentito se non loggato
      }),
      catchError((error) => {
        console.log("ERRORE durante la verifica dello stato di login:", error);
        return of(true); // Permette l'accesso in caso di errore
      })
    );
  }
}
