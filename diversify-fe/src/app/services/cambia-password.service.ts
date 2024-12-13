import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CambiaPasswordService {
  private apiUrl = 'http://localhost:8080/api/cambia_password';

  constructor(private http: HttpClient) {}

  changePassword(request: ChangePasswordRequest): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });        //Questa riga indica che il corpo della richiesta è in formato JSON
    return this.http.post(`${this.apiUrl}/cambia_password`, request, { headers });
  }
}

//Interfaccia:
//L'uso di un'interfaccia permette di tipizzare esplicitamente i dati inviati al backend
export interface ChangePasswordRequest {
  username: string;
  currentPassword: string;
  newPassword: string;
}
