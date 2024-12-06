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
	  confermaPassword: ['', Validators.required],
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
  
  // Funzione per la validazione personalizzata della password
  passwordsMustMatch(group: FormGroup): { [key: string]: boolean } | null {
	const password = group.get('password')?.value;
	const confermaPassword = group.get('confermaPassword')?.value;
	return password && confermaPassword && password !== confermaPassword
	  ? { passwordsMismatch: true }
	  : null;
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

  // Funzione per annullare l'eliminazione
  annullaEliminazione(): void {
    this.eliminaAccount = false; // Nascondi la sezione "Elimina Account"
  }
}
