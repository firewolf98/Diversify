import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
 
@Injectable({
  providedIn: 'root'
})
export class UserService {
 
  private apiUrl = 'http://localhost:8080/utenti';
 
  constructor(private http: HttpClient) {}
 
  token: string | null = null;
 
  getUserFromToken(token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
 
    return this.http.get<any>(`${this.apiUrl}/recupera_utente`, { headers });
  }
 
  setToken(tokenLogin:any) {
    this.token = tokenLogin;
  }
 
  getToken() {
    return this.token;
  }
 
  changePassword(token:string, currentPassword: string, newPassword: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
 
    const body = {
      currentPassword,
      newPassword
    };
 
    return this.http.post(`${this.apiUrl}/cambia_password`, body, { headers });
  }
}