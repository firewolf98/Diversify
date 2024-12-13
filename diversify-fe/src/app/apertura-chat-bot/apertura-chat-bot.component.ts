import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-apertura-chat-bot',
  standalone: true,
  templateUrl: './apertura-chat-bot.component.html',
  styleUrls: ['./apertura-chat-bot.component.css'],
  imports: [CommonModule],
})
export class AperturaChatBotComponent {
  isChatOpen = false;

  constructor() {}

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const target = event.target as HTMLElement;

    // Controlla se il clic avviene sulla classe associata all'icona
    if (target.closest('.brigid-icon-container')) {
      this.toggleChat();
    }
  }

  toggleChat(): void {
    this.isChatOpen = !this.isChatOpen;
  }
}
