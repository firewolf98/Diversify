import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ForumService {

  private apiUrl = 'http://localhost:8080/api/forums';
  private apiPostUrl = 'http://localhost:8080/posts';
  private apiCommentoUrl = 'http://localhost:8080/api/commenti';

  constructor(private http: HttpClient) {}

  getAllForums(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createForum(addForum: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/forums`, addForum);
  }

  getForumById(id: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  addForum(forum: any, ruolo: boolean): Observable<any> {
    const params = new HttpParams().set('ruolo', String(ruolo));
    return this.http.post<any>(this.apiUrl, forum, { params });
  }

  updateForum(id: string, updatedForum: any, ruolo: boolean): Observable<any> {
    const params = new HttpParams().set('ruolo', String(ruolo));
    return this.http.put<any>(`${this.apiUrl}/${id}`, updatedForum, { params });
  }

  deleteForum(id: string, ruolo: boolean): Observable<void> {
    const params = new HttpParams().set('ruolo', String(ruolo));
    return this.http.post<void>(`${this.apiUrl}/delete/${id}`, null, { params });
  }

  loadForums(country: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/by-paese/${country}`);
  }

  savePost(postData: { titolo: string; contenuto: string; idForum: string; idAutore: string }): Observable<any> {
    return this.http.post(`${this.apiPostUrl}/createPost`, postData);
  }

  getPost(forumId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiPostUrl}/forum/${forumId}`);
  }

  aggiungiCommento(idPost: string, commento: any): Observable<any> {
    return this.http.post<any>(`${this.apiCommentoUrl}/post/${idPost}`, commento);
  }
}
