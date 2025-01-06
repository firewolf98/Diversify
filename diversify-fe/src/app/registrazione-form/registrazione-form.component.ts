import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Observable, of } from 'rxjs'; // Aggiunto per mockare la verifica del database
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-registrazione-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './registrazione-form.component.html',
  styleUrls: ['./registrazione-form.component.css']
})
export class RegistrazioneFormComponent {
  moduloRegistrazione: FormGroup;

  isPasswordVisible: boolean = false;
  isConfermaPasswordVisible: boolean = false;

  constructor(private fb: FormBuilder, private authService: AuthService,    private router: Router ) {
    this.moduloRegistrazione = this.fb.group({
      nome: ['', [Validators.required, Validators.pattern('^[A-Z][a-z]*$')]], // Nome con prima lettera maiuscola
      cognome: ['', [Validators.required, Validators.pattern('^[A-Z][a-z]*$')]], // Cognome con prima lettera maiuscola
      domanda: ['', [Validators.required]],
      risposta: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]], // Email valida
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern('^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]+$'), // Controlla maiuscole, numeri, caratteri speciali
        ],
      ],
      confermaPassword: ['', [Validators.required]],
      cf: ['', [Validators.required, Validators.pattern('^[A-Za-z0-9]{16}$')]], // Codice fiscale di 16 caratteri
      username: ['', [Validators.required, Validators.minLength(5)]], // Username di almeno 5 caratteri
    }, {
      validators: this.passwordsMustMatch,  // Validatore per le password
      asyncValidators: [this.cfValidator(), this.usernameValidator()] 
    });
  }

  // Funzione per inviare i dati
  inviaDati(): void {
    //if (this.moduloRegistrazione.valid) {
      const formData = this.moduloRegistrazione.value;
      const user = {
        name: formData.nome,
        lastName: formData.cognome,
        username: formData.username,
        password: formData.password,
        email: formData.email,
        codiceFiscale: formData.cf,
        domanda: formData.domanda,
        risposta: formData.risposta
      };

      this.authService.register(user).subscribe(
        response => {
          alert(response.message);
          this.router.navigate(['/']);
        },
        error => {
          console.log('Errore nella registrazione', error);
        }
      );
    //} else {
      //console.log('Modulo non valido');
    //}
  }

  // Validatore per verificare se le password corrispondono
  passwordsMustMatch(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value;
    const confermaPassword = group.get('confermaPassword')?.value;

    if (password && confermaPassword && password !== confermaPassword) {
      return { passwordsNonCoincidenti: true };  // Ritorna errore se le password non corrispondono
    }
    return null;  // Restituisce null se le password sono valide
  }


  // Funzione per verificare se il Codice Fiscale esiste già (mock del database)
  verificaCodiceFiscaleEsistente(codiceFiscale: string): Observable<boolean> {
    // Simulazione di una chiamata al database per il codice fiscale
    const codiciFiscaliEsistenti = ['RSSMRA85M01H501Z', 'RSSMRA99M01H501X']; // Esempio
    return of(codiciFiscaliEsistenti.includes(codiceFiscale));
  }

  // Funzione per verificare se lo Username esiste già (mock del database)
  verificaUsernameEsistente(username: string): Observable<boolean> {
    // Simulazione di una chiamata al database per lo username
    const usernamesEsistenti = ['mario123', 'luigi456']; // Esempio
    return of(usernamesEsistenti.includes(username));
  }

  // Funzione per il controllo dell'errore personalizzato per Codice Fiscale
  cfValidator(): Validators {
    return (control: AbstractControl) => {
      const codiceFiscale = control.value;
      if (codiceFiscale) {
        this.verificaCodiceFiscaleEsistente(codiceFiscale).subscribe((exists) => {
          if (exists) {
            control.setErrors({ cfEsistente: true });
          }
        });
      }
      return null;
    };
  }

  // Funzione per il controllo dell'errore personalizzato per Username
  usernameValidator(): Validators {
    return (control: AbstractControl) => {
      const username = control.value;
      if (username) {
        this.verificaUsernameEsistente(username).subscribe((exists) => {
          if (exists) {
            control.setErrors({ usernameEsistente: true });
          }
        });
      }
      return null;
    };
  }

  togglePasswordVisibility(field: 'password' | 'confermaPassword'): void {
    if (field === 'password') {
      this.isPasswordVisible = !this.isPasswordVisible;
    } else if (field === 'confermaPassword') {
      this.isConfermaPasswordVisible = !this.isConfermaPasswordVisible;
    }
  }

    // Funzione per navigare alla pagina di registrazione
    navigateToLogin(): void {
      this.router.navigate(['/loggato']);
    }
  
    
} 