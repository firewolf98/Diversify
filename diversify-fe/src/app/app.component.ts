import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./header/header.component";
import { FooterComponent } from "./footer/footer.component";
import { AperturaChatBotComponent } from "./apertura-chat-bot/apertura-chat-bot.component";
import { MetodoCommentoComponent } from './metodo-commento/metodo-commento.component'; // Importa il componente
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent, AperturaChatBotComponent, MetodoCommentoComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'], 
})


export class AppComponent {
  title = 'diversify-fe';
}