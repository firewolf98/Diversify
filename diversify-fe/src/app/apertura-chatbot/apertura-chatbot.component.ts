import { Component, ElementRef, HostListener, ViewChild, AfterViewChecked, OnInit, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Subscription } from 'rxjs';

interface ChatMessage {
  sender: 'user' | 'bot';
  content: string;
}

@Component({
  selector: 'app-apertura-chat-bot',
  standalone: true,
  templateUrl: './apertura-chatbot.component.html',
  styleUrls: ['./apertura-chatbot.component.css'],
  imports: [CommonModule, FormsModule],
})
export class AperturaChatBotComponent implements AfterViewChecked, OnInit, OnDestroy {
  @ViewChild('chatMessagesContainer') chatMessagesContainer!: ElementRef;

  isChatOpen = false;
  isLogged = false; // Controllo dello stato di login
  messages: ChatMessage[] = [];
  userInput = '';
  chatbotUrl = 'http://localhost:5000/chat'; // URL dell'API della chatbot
  backendUrl = 'http://localhost:8080/chatbot/conversations'; // URL del backend

  userId: string = ''; // Variabile per l'ID utente
  authSubscription!: Subscription; // Sottoscrizione per gestire lo stato di login

  constructor(private http: HttpClient, private authService: AuthService) {
    this.messages.push({
      sender: 'bot',
      content: 'Start a conversation! English(UK) is the only supported language.',
    });
  }

  ngOnInit(): void {
    // Sottoscrizione per controllare lo stato di login
    this.authSubscription = this.authService.isLoggedIn().subscribe((status: boolean) => {
      this.isLogged = status;

      // Chiudi la chat se l'utente effettua il logout
      if (!this.isLogged) {
        this.isChatOpen = false;
        this.messages = []; // Reset dei messaggi
      }
    });

    // Recupera i dati dell'utente loggato
    this.authService.getUser().subscribe(
      (user) => {
        if (user && user.username) {
          this.userId = user.username; // Salva l'username dell'utente come userId
        }
      },
      (error) => {
        console.error('Errore nel recupero dell\'utente:', error);
      }
    );
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const target = event.target as HTMLElement;
    if (target.closest('.brigid-icon-container')) {
      this.toggleChat();
    }
  }

  toggleChat(): void {
    this.isChatOpen = !this.isChatOpen;
  }

  sendMessage(): void {
    if (this.userInput.trim() === '') {
      return; // Non inviare messaggi vuoti
    }

    // Aggiungi il messaggio dell'utente alla chat
    const userMessage = this.userInput;
    this.messages.push({ sender: 'user', content: userMessage });

    // Invia il messaggio dell'utente alla chatbot
    this.http.post<{ tag: string; response: string }>(this.chatbotUrl, { message: userMessage }).subscribe(
      (response) => {
        const botResponse = response.response;

        // Aggiungi la risposta della chatbot alla chat
        this.messages.push({ sender: 'bot', content: botResponse });

        // Prepara il payload con il timestamp
        const messagePayload = {
          userId: this.userId, // Usa l'ID dell'utente recuperato
          question: userMessage,
          answer: botResponse,
          timestamp: new Date().toISOString(), // Aggiungi il timestamp nel formato ISO
        };

        // Invia domanda e risposta al backend per il salvataggio
        this.http.post(this.backendUrl, messagePayload).subscribe();

        // Scorri automaticamente in basso
        this.scrollToBottom();
      },
      (error) => {
        // Gestione errori chatbot
        this.messages.push({ sender: 'bot', content: 'Communication is not possible. We apologize for the problem! :(' });
        this.scrollToBottom(); // Scorri in basso anche in caso di errore
      }
    );

    // Resetta l'input dell'utente
    this.userInput = '';
  }

  scrollToBottom(): void {
    if (this.chatMessagesContainer) {
      this.chatMessagesContainer.nativeElement.scrollTop =
        this.chatMessagesContainer.nativeElement.scrollHeight;
    }
  }

  ngAfterViewChecked(): void {
    // Assicura che lo scroll avvenga dopo ogni aggiornamento della vista
    this.scrollToBottom();
  }

  ngOnDestroy(): void {
    // Annulla la sottoscrizione per prevenire memory leaks
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
