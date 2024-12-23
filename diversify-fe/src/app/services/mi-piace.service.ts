import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MiPiaceService {

  private apiUrl = 'https://localhost:8080/api/mi-piace';
  
  constructor(private http: HttpClient) {}

  // Aggiunge un Mi Piace su un'entità (post, commento, subcommento)
  addMiPiace(entityId: string, entityType: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}`, { entityId, entityType });
  }

  // Rimuove un Mi Piace da un'entità
  removeMiPiace(entityId: string, entityType: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${entityType}/${entityId}`);
  }

  // Recupera il numero di Mi Piace per un'entità
  getMiPiaceCount(entityId: string, entityType: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count/${entityType}/${entityId}`);
  }

  // Verifica se l'utente ha già messo Mi Piace su un'entità
  checkIfMiPiace(entityId: string, entityType: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/check/${entityType}/${entityId}`);
  }

  // Invia una richiesta al backend per aggiornare lo stato del "Mi Piace" per un determinato post, commento o subcommento.
  toggleMiPiace(itemId: string, liked: boolean): Observable<any> {
    const payload = { itemId, liked }; // Corpo della richiesta
    return this.http.post<any>(`${this.apiUrl}/toggle`, payload); // Invio della richiesta al backend
  }
}
