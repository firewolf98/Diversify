import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service'; // Importa il servizio AuthService

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

  isPasswordVisible = false;

  constructor(private fb: FormBuilder,
    private router: Router,
    private authService: AuthService) {
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

      // Usa il servizio AuthService per eseguire il login
      this.authService.login(email, password).subscribe({
        next: (response) => {
          // Se il login è riuscito, reindirizza alla home
          console.log('Login successful:', response);
          this.router.navigate(['/home']);
        },
        error: (error) => {
          // Se c'è un errore (credenziali errate, errore di rete, ecc.), mostra un messaggio di errore
          console.error('Login failed:', error);
          this.errorMessage = 'Email o password errati';
        }
      });
    } else {
      this.errorMessage = 'Per favore, compila correttamente i campi.';
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

  /**
 * Funzione che abilita/disabilita la visibilità della password.
 * Accetta l'ID dell'elemento come parametro (per futura espandibilità).
 */
  togglePasswordVisibility(field: string): void {
    if (field === 'password') {
      this.isPasswordVisible = !this.isPasswordVisible;
    }
  }
}
