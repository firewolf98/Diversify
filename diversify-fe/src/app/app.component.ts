import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoginFormComponent } from "./login-form/login-form.component";
import { RegistrazioneFormComponent } from "./registrazione-form/registrazione-form.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LoginFormComponent, RegistrazioneFormComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'diversify-fe';
}
