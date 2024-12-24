import { Component } from '@angular/core';
import { IconaBrigidComponent } from "../icona-brigid/icona-brigid.component";
import { MapComponent } from "../mappa/mappa.component";
import { AperturaChatBotComponent } from "../apertura-chatbot/apertura-chatbot.component";

@Component({
  selector: 'app-homepage',
  standalone:true,
  imports: [IconaBrigidComponent, MapComponent, AperturaChatBotComponent],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.css'
})
export class HomepageComponent {

}
