import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
 
@Injectable({
  providedIn: 'root'
})
export class ForumService {
 
  private apiUrl = "http://localhost:8080/api/forums/by-paese";
 
  constructor(private http: HttpClient) { }
 
  loadForums(paese: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${paese}`);
  }
}