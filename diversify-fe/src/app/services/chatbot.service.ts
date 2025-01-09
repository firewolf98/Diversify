import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ChatbotService {
  private apiUrl = 'http://localhost:5000/chat'; // URL del server della chatbot

  constructor(private http: HttpClient) {}

  sendMessage(message: string): Observable<{ tag: string; response: string }> {
    return this.http.post<{ tag: string; response: string }>(this.apiUrl, { message });
  }
}
