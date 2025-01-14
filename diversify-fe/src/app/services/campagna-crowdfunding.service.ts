import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, tap, throwError } from 'rxjs';

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

  getAllCampagne(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl)   
  }
  

  createCampagna(campaign: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, campaign);
  }

  updateCampagna(campaign: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${campaign.idCampagna}`, campaign);
  }

  deleteCampagna(idCampagna: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${idCampagna}`);
  }
  
  
}
