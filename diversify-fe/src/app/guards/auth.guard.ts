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
        const restrictedPaths = ['scheda-area-personale', 'campagne']; // Aggiunto 'campagne' come rotta protetta
        const currentPath = route.routeConfig?.path || '';

        // Se non loggato
        if (!isLoggedIn) {
          if (
            currentPath === 'loggato' ||
            currentPath === 'registrato' ||
            currentPath === 'recupero-password'
          ) {
            return true; // Consenti l'accesso alle pagine di login o registrazione
          }
          console.log('ACCESS DENIED: Utente non loggato');
          this.router.navigate(['/loggato']);
          return false;
        }

        // Se loggato, consenti l'accesso alle rotte protette
        if (restrictedPaths.some((path) => currentPath.startsWith(path))) {
          console.log('ACCESS GRANTED: Utente loggato, accesso consentito alla rotta:', currentPath);
          return true;
        }

        console.log('ACCESS DENIED: Utente loggato, accesso non consentito a questa rotta:', currentPath);
        this.router.navigate(['/']); // Reindirizza alla homepage
        return false;
      }),
      catchError((error) => {
        console.log('ERRORE durante la verifica dello stato di login:', error);
        this.router.navigate(['/loggato']);
        return of(false); // Impedisce l'accesso in caso di errore
      })
    );
  }
}
