import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CampagnaService {
  private apiUrl = 'http://localhost:8080/api/campagne'; // Base URL del backend

  constructor(private http: HttpClient) {}

  getCampagneByPaese(paese: string): Observable<any[]> {
    const url = `${this.apiUrl}/by-paese/${paese}`;
    return this.http.get<any[]>(url);
  }

  getCampagnaById(idCampagna: string): Observable<any> {
    const url = `${this.apiUrl}/${idCampagna}`;
    return this.http.get<any>(url);
  }
  
}
