import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common'; // Necessario per usare i pipes come date

@Component({
  selector: 'app-commento',
  standalone: true,
  templateUrl: './commento.component.html',
  styleUrls: ['./commento.component.css'],
  imports: [CommonModule] // Importa CommonModule per abilitare il pipe date
})
export class CommentoComponent {
  @Input() comment!: { // Aggiungiamo Input per ricevere il commento dal componente padre
    authorName: string;
    authorAvatar: string;
    text: string;
    date: string;
  };

  isReplying: boolean = false; // Gestisce la visualizzazione del form di risposta

  onReply(): void {
    this.isReplying = !this.isReplying; // Mostra/nasconde il modulo di risposta
  }

  submitReply(replyText: string): void {
    if (replyText.trim()) {
      console.log('Nuova risposta:', replyText);
      // Puoi inviare questa risposta al backend o aggiungerla localmente
      this.isReplying = false; // Nascondi il modulo dopo l'invio
    }
  }
}
