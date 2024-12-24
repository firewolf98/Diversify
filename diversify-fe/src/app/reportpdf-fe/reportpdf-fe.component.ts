import { Component, OnInit, OnDestroy } from '@angular/core';
import jsPDF from 'jspdf';

@Component({
  selector: 'app-reportpdf',
  standalone: true,
  templateUrl: './reportpdf-fe.component.html',
  styleUrls: ['./reportpdf-fe.component.css']
})
export class ReportPDFComponent implements OnInit, OnDestroy {
  countryData = {
    name: 'Italy',
  };

  constructor() {}

  ngOnInit(): void {
    // Registra un listener per l'evento
    window.addEventListener('download-pdf', this.generatePDF.bind(this));
  }

  ngOnDestroy(): void {
    // Rimuove il listener per evitare memory leaks
    window.removeEventListener('download-pdf', this.generatePDF.bind(this));
  }

  generatePDF(): void {
    const doc = new jsPDF();

    doc.setFontSize(16);
    doc.text('Report Paese', 105, 10, { align: 'center' });

    doc.setFontSize(12);
    doc.text(`Nome Paese: ${this.countryData.name}`, 10, 30);

    doc.save(`${this.countryData.name}_Report.pdf`);
  }
}
