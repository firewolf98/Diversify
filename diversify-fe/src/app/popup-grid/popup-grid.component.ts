import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ForumService } from '../services/forum.service';
import { DocumentoInformativoService } from '../services/documento-informativo.service';

@Component({
  selector: 'app-popup-grid',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './popup-grid.component.html',
  styleUrls: ['./popup-grid.component.css'],
})
export class PopupGridComponent {
  constructor(
    private router: Router,
    private forumService: ForumService,
    private documentoInformativoService: DocumentoInformativoService
  ) {}

  @Input() tipoCriticita: string = '';
  @Input() countryName: string = '';
  @Input() flag: string = '';
  @Input() idPaese: string = ''; // ID del paese ricevuto come input.
  @Output() closePopup = new EventEmitter<void>();

  isVisible: boolean = true; // Variabile di stato per gestire la visibilità del popup.
  forum: any;
  documentiInformativi: any[] = []; // Per salvare i documenti informativi dal backend.
  videoLink: string | null = null; // Link del video caricato dal backend.

  buttons: { label: string; color: string; size: string; action?: () => void }[] = [];

  ngOnInit() {
    this.loadForumData();
    this.loadDocumentiInformativi();
  }

  private loadForumData(): void {
    this.forumService.loadForums(this.countryName).subscribe((data) => {
      this.forum = data;
      this.updateButtons();
    });
  }

  private loadDocumentiInformativi(): void {
    if (this.idPaese) {
      console.log('Effettuando chiamata per il paese:', this.idPaese); // Debug iniziale
      this.documentoInformativoService.getDocumentiInformativi(this.idPaese).subscribe(
        (data) => {
          console.log('Chiamata completata con successo');
          this.documentiInformativi = data;
          console.log('Documenti Informativi ricevuti:', this.documentiInformativi);
          this.videoLink = data.length > 0 ? data[0].linkVideo : null;
          console.log('Link Video:', this.videoLink);
          this.updateButtons();
        },
        (error) => {
          console.error('Errore nel caricamento dei documenti informativi:', error);
        }
      );
    } else {
      console.log('ID Paese non fornito.');
    }
  }

  private updateButtons(): void {
    this.buttons = [
      {
        label: `Documenti informativi`,
        color: 'pink',
        size: 'medium',
        action: () => {
          this.closePopup.emit();
          this.router.navigate(['/documentoinformativo', this.idPaese]);
        },
      },
      ...(this.forum && this.forum.length > 0
        ? [
            {
              label: this.forum[0].titolo,
              color: 'green',
              size: 'medium',
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
        ...(this.forum && this.forum.length > 1
          ? [
              {
                label: "Tutti i forum",
                color: 'pink',
                size: 'medium',
                action: () => this.goToForum(),
              },
            ]
          : []),
      {
        label: `Tutte le campagne di crowdfunding`,
        color: 'blue',
        size: 'medium',
        action: () => this.goToAllCampagne(),
      },
      {
        label: `Campagna di crowdfunding: ${this.countryName}`,
        color: 'green',
        size: 'large',
        action: () => this.goToSpecificCampagna('CAMP01'), // Passa l'ID specifico
      },
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

  openVideo(link: string): void {
    window.open(link, '_blank'); // Apre il link video in una nuova scheda.
  }

  // Naviga alla lista completa delle campagne di crowdfunding.
  private goToAllCampagne(): void {
    this.closePopup.emit();
    this.router.navigate(['/campagne', this.countryName]);
  }

  // Naviga a una specifica campagna di crowdfunding.
private goToSpecificCampagna(idCampagna: string): void {
  this.closePopup.emit();
  this.router.navigate(['/campagne', this.countryName], {
    queryParams: { idCampagna },
  });
}
}
