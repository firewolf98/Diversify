import { Component, OnInit } from '@angular/core'; // Aggiunto OnInit
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { IconaBrigidComponent } from "../icona-brigid/icona-brigid.component";
import { MapComponent } from "../mappa/mappa.component";
import { AperturaChatBotComponent } from "../apertura-chatbot/apertura-chatbot.component";
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [CommonModule, IconaBrigidComponent, MapComponent, AperturaChatBotComponent], // Aggiunto CommonModule
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css'],
})
export class HomepageComponent implements OnInit {
  isLogged: boolean = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.isLoggedIn().subscribe((status) => {
      this.isLogged = status;
    });
  }
}

