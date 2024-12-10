import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Observable, of } from 'rxjs'; // Per simulare la verifica del database

@Component({
  selector: 'app-scheda-area-personale',
  standalone: true,  // Dichiarazione del componente come standalone
  imports: [CommonModule, ReactiveFormsModule],  // Aggiungi CommonModule qui
  templateUrl: './scheda-area-personale.component.html',
  styleUrls: ['./scheda-area-personale.component.css']
})

export class SchedaAreaPersonaleComponent {
  // Dichiarazione dei form
  modificaPassword: boolean = false;
  eliminaAccount: boolean = false;

  isVecchiaPasswordVisible = false;
  isPasswordVisible = false;
  isConfermaPasswordVisible = false;
  isPasswordEliminazioneVisible = false;

  formModificaPassword: FormGroup;
  formEliminaAccount: FormGroup;

  // Dati dell'utente
  username: string = 'utente123';
  nome: string = 'Mario';
  cognome: string = 'Rossi';
  email: string = 'mario.rossi@example.com';
  domandaPersonale: string = 'Nome del tuo primo animale?';

  constructor(private fb: FormBuilder) {
	// Inizializzazione dei form
	this.formModificaPassword = this.fb.group({
	  vecchiaPassword: [
		'',
		[
		  Validators.required,
		  Validators.minLength(8), // Minimo 8 caratteri
		  Validators.pattern('^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]+$'), // Pattern per la complessità della password
		],
	  ],
	  password: [
		'',
		[
		  Validators.required,
		  Validators.minLength(8),
		  Validators.pattern('^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]+$'),
		],
	  ],
	  confermaPassword: ['',
    [
      Validators.required,      
    ], 
    ],
	}, { validators: this.passwordsMustMatch });
  
	this.formEliminaAccount = this.fb.group({
	  passwordEliminazione: [
		'',
		[
		  Validators.required, 
		  Validators.minLength(8), 
		  Validators.pattern('^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]+$'),
		],
	  ],
	});
  }
  
  passwordsMustMatch(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value;
    const confermaPassword = group.get('confermaPassword')?.value;
    if (password && confermaPassword && password !== confermaPassword) {
      return { passwordsNonCoincidenti: true };  // Ritorna errore se le password non corrispondono
    }
    return null;  // Restituisce null se le password sono valide
  }
  
  // Funzione per inviare la modifica della password
  salvaModificaPassword(): void {
    if (this.formModificaPassword.valid) {
      console.log('Nuova password salvata');
      // Aggiungi la logica per salvare la nuova password
    } else {
      console.log('Errore: le password non corrispondono o sono incomplete');
    }
  }

  // Funzione per visualizzare/nascondere il form di modifica password
  toggleModificaPassword(): void {
    this.modificaPassword = !this.modificaPassword;
    this.eliminaAccount = false; // Nascondi la sezione "Elimina Account"
  }

  // Funzione per visualizzare/nascondere il form di eliminazione account
  toggleEliminaAccount(): void {
    this.eliminaAccount = !this.eliminaAccount;
    this.modificaPassword = false; // Nascondi la sezione "Modifica Password"
  }

  // Funzione per confermare l'eliminazione dell'account
  confermaEliminazione(): void {
    if (this.formEliminaAccount.valid) {
      console.log('Form valido, procedere con l’eliminazione!');
    } else {
      console.log('Form non valido, correggere gli errori.');
    }
  }

    // Metodo per alternare la visibilità della password
   togglePasswordVisibility(field: string) {
    if (field === 'vecchiaPassword') {
      this.isVecchiaPasswordVisible = !this.isVecchiaPasswordVisible;
    } else if (field === 'password') {
      this.isPasswordVisible = !this.isPasswordVisible;
    } else if (field === 'confermaPassword') {
      this.isConfermaPasswordVisible = !this.isConfermaPasswordVisible;
    } else if (field === 'passwordEliminazione') {
      this.isPasswordEliminazioneVisible = !this.isPasswordEliminazioneVisible;
    }
  }

  // Funzione per annullare l'eliminazione
  annullaEliminazione(): void {
    this.eliminaAccount = false; // Nascondi la sezione "Elimina Account"
  }
}
