import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CampagnaService {
  private apiUrl = 'http://localhost:8080/api/campagne'; // Base URL del backend

  constructor(private http: HttpClient) {}

  getCampagneByPaese(paese: string): Observable<any[]> {
    const url = `${this.apiUrl}/by-paese/${paese}`;
    return this.http.get<any[]>(url).pipe(
      catchError((error) => this.handleHttpError(error, [])) // Restituisce un array vuoto se 404
    );
  }

  getCampagnaById(idCampagna: string): Observable<any> {
    const url = `${this.apiUrl}/${idCampagna}`;
    return this.http.get<any>(url).pipe(
      catchError((error) => this.handleHttpError(error)) // Restituisce `null` se 404
    );
  }

  getAllCampagne(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl).pipe(
      catchError((error) => this.handleHttpError(error, [])) // Restituisce un array vuoto se 404
    );
  }

  createCampagna(campaign: any): Observable<any> {
    const url = `${this.apiUrl}/create`;
    return this.http.post<any>(url, campaign).pipe(
      catchError((error) => this.handleHttpError(error)) // Propaga l'errore
    );
  }

  updateCampagna(campaign: any): Observable<any> {
    const url = `${this.apiUrl}/update`;
    return this.http.put<any>(`${url}/${campaign.idCampagna}`, campaign).pipe(
      catchError((error) => this.handleHttpError(error)) // Propaga l'errore
    );
  }

  deleteCampagna(idCampagna: string): Observable<any> {
    const url = `${this.apiUrl}/delete`;
    return this.http.post<any>(url,idCampagna).pipe(
      catchError((error) => this.handleHttpError(error)) // Propaga l'errore
    );
  }

  private handleHttpError<T>(error: any, defaultValue: T | null = null): Observable<T> {
    if (error.status === 404) {
      console.warn('Risorsa non trovata:', error.message);
      return new Observable((observer) => {
        if (defaultValue !== null) {
          observer.next(defaultValue); // Restituisce il valore predefinito
        }
        observer.complete();
      });
    }
  
    console.error('Errore HTTP:', error);
    return throwError(() => new Error(error.message || 'Errore del server'));
  }
} 
