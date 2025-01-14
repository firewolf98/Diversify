import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-documento-informativo',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './documento-informativo.component.html',
  styleUrls: ['./documento-informativo.component.css']
})
export class DocumentoInformativoComponent {
  documents: string[] = [
    'Documento 1 (Placeholder)',
    'Documento 2 (Placeholder)',
    'Documento 3 (Placeholder)'
  ];
  
  // Array per i video
  videos: { name: string, url: string }[] = [
    { name: 'Video 1 - Introduzione', url: 'https://www.youtube.com/watch?v=video1' },
    { name: 'Video 2 - Tutorial', url: 'https://www.youtube.com/watch?v=video2' },
    { name: 'Video 3 - Approfondimento', url: 'https://www.youtube.com/watch?v=video3' }
  ];

  // Array per i testi
  texts: { name: string, url: string }[] = [
    { name: 'TESTO 1', url: 'https://example.com/testo1' },
    { name: 'TESTO 2', url: 'https://example.com/testo2' },
    { name: 'TESTO 3', url: 'https://example.com/testo3' }
  ];
  
  selectedDocument: string | null = null;
  selectedVideo: string | null = null;
  selectedText: string | null = null;

  onDocumentChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedDocument = selectElement.value;
  }

  onVideoChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const selectedVideo = this.videos.find(video => video.name === selectElement.value);
    if (selectedVideo) {
      window.open(selectedVideo.url, '_blank');
    }
  }

  // Funzione per gestire il cambio del menù dei testi
  onTextChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const selectedText = this.texts.find(text => text.name === selectElement.value);
    if (selectedText) {
      window.open(selectedText.url, '_blank');
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
