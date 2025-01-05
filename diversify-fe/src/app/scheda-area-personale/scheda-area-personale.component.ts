import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Observable, of } from 'rxjs'; // Per simulare la verifica del database
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
 
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
    utente: any;
    token: string | null = null;
 
    // Dati dell'utente
    username: string = '';
    nome: string = '';
    cognome: string = '';
    email: string = '';
    domandaPersonale: string = '';
 
    constructor(private fb: FormBuilder, private userService:UserService) {
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
 
    ngOnInit(): void {
        this.token = localStorage.getItem('auth_token');
 
        if (this.token) {
        this.userService.getUserFromToken(this.token).subscribe(
            (data) => {
            this.utente = data;
            console.log(this.utente);
            this.username= this.utente.username;
            this.nome= this.utente.nome;
            this.cognome= this.utente.cognome;
            this.email= this.utente.email;
            this.domandaPersonale= this.utente.tipoDomanda;
            },
            (error) => {
            console.error('Errore nel recupero dei dati utente:', error);
            }
        );
        } else {
        console.error('Token non trovato.');
        }
    };
 
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
        if (this.formModificaPassword.valid && this.token) {
            const vecchiaPassword = this.formModificaPassword.get('vecchiaPassword')?.value;
            const nuovaPassword = this.formModificaPassword.get('password')?.value;
            this.userService.changePassword(this.token, vecchiaPassword, nuovaPassword).subscribe({
                next: (response) => {
                  alert(response.message);
                },
                error: (error) => {
                  alert(error);
                }
              });
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