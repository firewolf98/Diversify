import { Component, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DocumentoInformativoService } from '../services/documento-informativo.service';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import jsPDF from 'jspdf';

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

  onTextChange(event: Event): void {
    const selectedDoc = this.docs.find((doc) => doc.idDocumentoInformativo === this.selectedValue);
    this.videoContainer.nativeElement.innerHTML = '';
  
    if (selectedDoc) {
      console.log(selectedDoc); // Debug: verifica il contenuto di selectedDoc
      if (selectedDoc.descrizione) {
        // Mostra descrizione e immagine se presenti
        this.selectedDocument = {
          image: selectedDoc.linkImmagine, // URL dell'immagine
          content: selectedDoc.descrizione // Contenuto HTML
        };
      } else if (selectedDoc.linkVideo) {
        // Crea un video dinamico se descrizione è vuota o undefined
        const videoElement = this.renderer.createElement('video');
        this.renderer.setAttribute(videoElement, 'width', '640');
        this.renderer.setAttribute(videoElement, 'height', '360');
        this.renderer.setAttribute(videoElement, 'controls', 'true');
  
        // Creazione dinamica della sorgente video
        const sourceElement = this.renderer.createElement('source');
        this.renderer.setAttribute(sourceElement, 'src', selectedDoc.linkVideo); // Usa il linkVideo
        this.renderer.setAttribute(sourceElement, 'type', 'video/mp4');
  
        // Aggiungi la sorgente al video
        this.renderer.appendChild(videoElement, sourceElement);
        this.renderer.appendChild(this.videoContainer.nativeElement, videoElement);
  
        // Cancella il contenuto testuale selezionato
        this.selectedDocument = null;
      } else {
        this.selectedDocument = {
          content: 'Nessun contenuto disponibile per questo documento.'
        };
      }
    } else {
      this.selectedDocument = {
        content: 'Documento non trovato.'
      };
    }
  }

  async downloadPDF(): Promise<void> {
    const pdf = new jsPDF('p', 'mm', 'a4'); // Formato A4, orientamento verticale
    const pageWidth = pdf.internal.pageSize.getWidth(); // Larghezza della pagina
    const pageHeight = pdf.internal.pageSize.getHeight(); // Altezza della pagina
    const margin = 20; // Margine sinistro e destro
    const lineHeight = 8; // Altezza della linea di testo
    const logoUrl = 'Logodiversify.png'; // Percorso del logo (assicurati che sia corretto)
    const currentDate = new Date().toLocaleDateString(); // Data di stampa
    const selectedDocument = this.docs.find((doc) => doc.idDocumentoInformativo === this.selectedValue);
  
    // Funzione per rimuovere i tag HTML e convertire il testo
    const stripHtmlTags = (html: string): string => {
      const doc = new DOMParser().parseFromString(html, 'text/html');
      return doc.body.textContent || ''; // Restituisce il testo senza tag HTML
    };
  
    // Funzione per caricare un'immagine da un URL esterno
    const loadImageFromUrl = async (url: string): Promise<HTMLImageElement> => {
      return new Promise((resolve, reject) => {
        const img = new Image();
        img.crossOrigin = 'Anonymous'; // Permette il caricamento di immagini da altri domini
        img.src = url;
        img.onload = () => resolve(img);
        img.onerror = () => reject(new Error(`Errore nel caricamento dell'immagine: ${url}`));
      });
    };
  
    // Funzione per aggiungere un'immagine al PDF
    const addImageToPdf = async (imageUrl: string, x: number, y: number, width: number, height: number): Promise<number> => {
      try {
        const img = await loadImageFromUrl(imageUrl); // Carica l'immagine
        pdf.addImage(img, 'JPEG', x, y, width, height); // Aggiungi l'immagine al PDF
        return height + 10; // Restituisce l'altezza aggiunta
      } catch (error) {
        console.error('Errore nel caricamento dell\'immagine:', error);
        return 0; // Se l'immagine non viene caricata, non aggiunge nulla
      }
    };
  
    try {
      // Aggiungi il logo in testata
      const logoWidth = 10; // Larghezza del logo
      const logoHeight = 10; // Altezza del logo
      await addImageToPdf(logoUrl, margin, 10, logoWidth, logoHeight); // Aggiungi il logo
  
      // Aggiungi la data di stampa in alto a destra
      pdf.setFontSize(10);
      pdf.setTextColor(100); // Colore grigio per la data
      pdf.text(`Data di stampa: ${currentDate}`, pageWidth - margin, 20, { align: 'right' });
  
      // Aggiungi il titolo dinamico del documento
      if (selectedDocument?.titolo) {
        pdf.setFontSize(22);
        pdf.setTextColor(0, 0, 0); // Colore nero per il titolo
        pdf.setFont('helvetica', 'bold');
        const title = selectedDocument.titolo; // Titolo dinamico
        pdf.text(title, margin, 50); // Posizione del titolo
      }
  
      // Aggiungi il contenuto dinamico da selectedDocument
      pdf.setFontSize(12);
      pdf.setTextColor(0, 0, 0); // Colore nero per il testo
      pdf.setFont('helvetica', 'normal');
  
      // Funzione per aggiungere testo formattato e gestire pagine multiple
      const addFormattedText = (text: string, x: number, y: number) => {
        const cleanedText = stripHtmlTags(text); // Rimuove i tag HTML
        const splitText = pdf.splitTextToSize(cleanedText, pageWidth - 2 * margin); // Divide il testo in base alla larghezza della pagina
  
        let currentY = y;
        for (const line of splitText) {
          // Se il testo supera l'altezza della pagina, aggiungi una nuova pagina
          if (currentY > pageHeight - margin) {
            pdf.addPage(); // Aggiungi una nuova pagina
            currentY = margin; // Ripristina la posizione Y
          }
        
          pdf.text(line, x, currentY); // Aggiungi la linea di testo
          currentY += lineHeight; // Sposta la posizione Y
        }
  
        return currentY; // Restituisce la nuova posizione Y
      };
  
      // Posizione iniziale per il contenuto
      let yPosition = 60;
  
      // Aggiungi l'immagine se presente
      if (selectedDocument?.linkImmagine) {
        const imgHeight = await addImageToPdf(selectedDocument.linkImmagine, margin, yPosition, 100, 60); // Aggiungi l'immagine
        yPosition += imgHeight; // Sposta la posizione Y dopo l'immagine
      }
  
      // Aggiungi la descrizione se presente
      if (selectedDocument?.descrizione) {
        yPosition = addFormattedText(selectedDocument.descrizione, margin, yPosition) + 10;
      }
  
      // Aggiungi il footer
      pdf.setFontSize(10);
      pdf.setTextColor(100); // Colore grigio per il footer
      pdf.text('Diversify - Una piattaforma per combattere la discriminazione', margin, pageHeight - 10);
  
      // Aggiungi i numeri di pagina
      const pageCount = pdf.getNumberOfPages();
      for (let i = 1; i <= pageCount; i++) {
        pdf.setPage(i);
        pdf.setFontSize(10);
        pdf.setTextColor(100);
        pdf.text(`Pagina ${i} di ${pageCount}`, pageWidth - margin, pageHeight - 10, { align: 'right' });
      }
  
      // Salva il PDF
      pdf.save(`${selectedDocument?.titolo || 'documento'}.pdf`); // Nome del file basato sul titolo
    } catch (error) {
      console.error('Errore nella generazione del PDF:', error);
    }
  }

  isVideoContainerEmpty(): boolean {
    return !this.videoContainer || this.videoContainer.nativeElement.children.length === 0;
  }
  
}