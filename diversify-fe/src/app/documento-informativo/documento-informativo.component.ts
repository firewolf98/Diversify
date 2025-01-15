import { Component, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DocumentoInformativoService } from '../services/documento-informativo.service';
import { ActivatedRoute } from '@angular/router';
import { data, error } from 'cypress/types/jquery';
import { FormsModule } from '@angular/forms';
import { any } from 'cypress/types/bluebird';

@Component({
  selector: 'app-documento-informativo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './documento-informativo.component.html',
  styleUrls: ['./documento-informativo.component.css']
})
export class DocumentoInformativoComponent {
  
  @ViewChild('videoContainer', { static: true }) videoContainer!: ElementRef;
  selectedDocument: any;
  docs: any[]= [];
  paese: any;
  selectedValue: any;

  constructor (private documentoInformativoService: DocumentoInformativoService, private route: ActivatedRoute, private renderer: Renderer2) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      this.paese = params.get('paese') || ''; // Ottieni il parametro 'paese'
      this.documentoInformativoService.getDocumentiInformativi(this.paese).subscribe(
        (data) => {
          this.docs= data;
        },
        (error) => {
          console.log("Errore nel recupero dei dati: ", error);
        },
      );
    });
  }

  onDocumentChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedDocument = selectElement.value;
  }

  // Funzione per gestire il cambio del menù dei testi
  onTextChange(event: Event): void {
    const selectElement = this.docs.find(item=> item.idPaese===this.selectedValue);
    if(selectElement?.descrizione != undefined)
      this.selectedDocument=selectElement.descrizione;
    else {
      const videoElement = this.renderer.createElement('video');
      this.renderer.setAttribute(videoElement, 'width', '640');
      this.renderer.setAttribute(videoElement, 'height', '360');
      this.renderer.setAttribute(videoElement, 'controls', 'true');

    // Creazione dinamica della sorgente video
      const sourceElement = this.renderer.createElement('source');
      this.renderer.setAttribute(sourceElement, 'src', 'https://www.w3schools.com/html/mov_bbb.mp4');
      this.renderer.setAttribute(sourceElement, 'type', 'video/mp4');

    // Aggiungi la sorgente al video
      this.renderer.appendChild(videoElement, sourceElement);
      this.renderer.appendChild(this.videoContainer.nativeElement, videoElement);

    //  this.selectedDocument= videoElement; 
    }
  }

  downloadPDF(): void {
    const pdfUrl = 'https://example.com/document.pdf';
    const link = document.createElement('a');
    link.href = pdfUrl;
    link.target = '_blank';
    link.download = 'documento_informativo.pdf';
    link.click();
  }
}
