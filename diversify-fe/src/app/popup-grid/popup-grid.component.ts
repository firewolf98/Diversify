import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ForumService } from '../services/forum.service';

@Component({
  selector: 'app-popup-grid',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './popup-grid.component.html',
  styleUrls: ['./popup-grid.component.css'],
})
export class PopupGridComponent {
  constructor(private router: Router, private forumService: ForumService) {}
  @Input() tipoCriticita: string = '';
  @Input() countryName: string = '';
  @Input() flag: string = '';
  @Output() closePopup = new EventEmitter<void>();

  isVisible: boolean = true; // Variabile di stato per gestire la visibilità del popup
  forum: any;

  buttons: { label: string; color: string; size: string; action?: () => void }[] = [];

  ngOnInit() {
    this.forumService.loadForums(this.countryName).subscribe((data) => {
      this.forum = data;
      this.updateButtons();
    });
  }

  private updateButtons(): void {
    this.buttons = [
      { label: `Leggi di ${this.countryName} sulla sicurezza e l'inclusività`, color: 'pink', size: 'large' },
      ...(this.forum && this.forum.length > 0
        ? [
            {
              label: this.forum[0].titolo,
              color: 'green',
              size: 'large',
              action: () => this.goToSpecificForum(this.forum[0].idForum),
            },
          ]
        : []),
      ...(this.forum && this.forum.length > 1
        ? [
            {
              label: this.forum[1].titolo,
              color: 'green',
              size: 'large',
              action: () => this.goToSpecificForum(this.forum[1].idForum),
            },
          ]
        : []),
      { label: `Tutti i forum su ${this.countryName}`, color: 'blue', size: 'small', action: () => this.goToForum() },
      { label: `Campagna di crowdfunding: Ċentru għall-persuni b'diżabilità f'${this.countryName}`, color: 'green', size: 'medium' },
      { label: `Tutte le campagne di crowdfunding`, color: 'blue', size: 'medium' },
    ];
  }

  goBack() {
    this.closePopup.emit();
  }

  goToForum() {
    this.closePopup.emit();
    this.router.navigate(['/forum'], {
      queryParams: { country: this.countryName },
    });
  }

  goToSpecificForum(forumId: string) {
    this.closePopup.emit();
    this.router.navigate(['/forum'], {
      queryParams: { country: this.countryName, forumId: forumId },
    });
  }


}
