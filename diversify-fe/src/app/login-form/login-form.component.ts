import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'],
  standalone: true, //viene utilizzata per dichiarare che un componente non dipende da un modulo di livello superiore
  imports: [CommonModule, ReactiveFormsModule] 
})
export class LoginFormComponent {
  loginForm: FormGroup;  // FormGroup per gestire il form di login
  errorMessage: string = '';  // Variabile per memorizzare i messaggi di errore

  constructor(private fb: FormBuilder, private router: Router) {
    // Inizializza il form di login con i campi email e password
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],  // L'email è obbligatoria e deve essere un'email valida
      password: ['', [
        Validators.required, // La password è obbligatoria
        Validators.minLength(8), // La password deve avere almeno 8 caratteri
        Validators.pattern('^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]+$')  // La password deve contenere almeno una lettera maiuscola, una minuscola, un numero e un carattere speciale
      ]],
    });
  }

  // Funzione chiamata quando l'utente invia il form
  onSubmit(): void {
    if (this.loginForm.valid) {  // Se il form è valido
      const email = this.loginForm.get('email')?.value;  // Ottieni il valore dell'email
      const password = this.loginForm.get('password')?.value;  // Ottieni il valore della password
      this.login(email, password);  // Chiama la funzione login per verificare le credenziali
    } else {
      // Se il form non è valido, mostra un messaggio di errore
      this.errorMessage = 'Per favore, compila correttamente i campi.';
    }
  }

  // Funzione per verificare le credenziali
  login(email: string, password: string): void {
    const validEmail = 'test@example.com';
    const validPassword = 'Password123!';

    if (email === validEmail && password === validPassword) {
      // Se le credenziali sono corrette, naviga alla home
      this.router.navigate(['/home']);
    } else {
      // Se le credenziali sono errate, mostra un messaggio di errore
      this.errorMessage = 'Email o password errati';
    }
  }

  // Funzione per verificare se un controllo del form ha un errore specifico
  hasError(controlName: string, errorCode: string): boolean {
    const control = this.loginForm.get(controlName);  
    return ((control?.dirty || control?.touched) && control?.hasError(errorCode)) ?? false;  
  }

  // Funzione per navigare alla pagina di recupero password
  recoverPassword(): void {
    this.router.navigate(['/recover-password']); 
  }
}
