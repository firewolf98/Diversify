import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PopupService {
  private closePopupSubject = new Subject<void>();

  closePopup$ = this.closePopupSubject.asObservable();

  closePopup(): void {
    this.closePopupSubject.next();
  }
}
