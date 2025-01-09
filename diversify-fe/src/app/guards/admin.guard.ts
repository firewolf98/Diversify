import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {
    return this.authService.getUser().pipe(
      map((user) => {
        if (user?.ruolo) {
          console.log('ACCESS GRANTED: Utente amministratore');
          return true; // L'utente è un amministratore
        } else {
          console.log('ACCESS DENIED: Utente non amministratore');
          this.router.navigate(['/']); // Reindirizza alla homepage
          return false;
        }
      }),
      catchError(() => {
        console.log('ERRORE: Impossibile verificare il ruolo dell\'utente');
        this.router.navigate(['/']);
        return of(false);
      })
    );
  }
}
