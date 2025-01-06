import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
 
@Injectable({
  providedIn: 'root'
})
export class CountryService {
  private apiUrl = 'http://localhost:8080/api/paesi';
 
  constructor(private http: HttpClient) {}
 
  getCountries(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
 