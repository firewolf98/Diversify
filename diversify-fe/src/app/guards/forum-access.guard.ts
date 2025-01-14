import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ForumAccessGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  /**
   * Controlla se l'utente può attivare la rotta.
   */
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {
    const forumId = route.params['forumId']; // Assumi che il forumId sia passato nell'URL

    return this.authService.getUser().pipe(
      map((user) => {
        if (!user) {
          console.log("NOT USER: Utente non loggato");
          this.router.navigate(['/loggato']); // Reindirizza alla login
          return false;
        }

        if (user.banned) {
          console.log("BANNED USER: Utente bannato da tutti i forum");
          alert("SEI STATO BANNATO PER COMPORTAMENTO SCORRETTO. VERRAI RIABILITATO IN UN SECONDO MOMENTO.")
          this.router.navigate(['/']); // Reindirizza alla homepage
          return false;
        }

        console.log("ACCESS GRANTED: Utente loggato e autorizzato");
        return true; // Accesso consentito
      }),
      catchError((error) => {
        console.log("ERRORE durante il recupero dell'utente:", error);
        this.router.navigate(['/loggato']); // Reindirizza alla login in caso di errore
        return of(false);
      })
    );
  }
}
