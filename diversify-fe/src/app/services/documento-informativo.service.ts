import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DocumentoInformativoService {
  private apiUrl = 'http://localhost:8080/api/paesi'; // Modifica l'URL in base alla tua configurazione backend.

  constructor(private http: HttpClient) {}

  getDocumentiInformativi(idPaese: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${idPaese}/documenti-informativi`).pipe(
      tap(data => console.log('Documenti ricevuti:', data)),
      catchError(error => {
        console.error('Errore nella chiamata:', error);
        return throwError(() => new Error('Errore nella chiamata API'));
      })
    );
  }
  
}
