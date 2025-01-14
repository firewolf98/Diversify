import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder, AbstractControl, ValidationErrors } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-gestione-password-form',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './gestione-password-form.component.html',
  styleUrls: ['./gestione-password-form.component.css'],
})
export class GestionePasswordFormComponent {
  currentStep: string = 'forgotPassword'; // Step iniziale
  forgotPasswordForm: FormGroup;
  securityQuestionForm: FormGroup;
  resetPasswordForm: FormGroup;

  email: string = '';
  fiscalCode: string = '';
  domandaPersonale: string = '';
  utente: any;
  risposta: string = '';

  // Variabili per la visibilità delle password
  isNewPasswordVisible: boolean = false;
  isConfirmPasswordVisible: boolean = false;

  // Regex per validare l'email (standard)
  private emailPattern: string = '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$';

  // Regex per validare il codice fiscale (italiano - semplificata)
  private fiscalCodePattern: string = '^[A-Za-z0-9]{16}$';

  constructor(private fb: FormBuilder, private router: Router, private userService: UserService) {
    // Inizializzazione del form per email e codice fiscale
    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.pattern(this.emailPattern)]],
      fiscalCode: [
        '',
        [
          Validators.required,
          Validators.pattern(this.fiscalCodePattern),
          Validators.minLength(16),
          Validators.maxLength(16),
          this.uppercaseValidator,
        ],
      ],
    });

    // Inizializzazione del form per la domanda di sicurezza
    this.securityQuestionForm = this.fb.group({
      securityAnswer: ['', Validators.required],
    });

    // Inizializzazione del form per la reimpostazione della password
    this.resetPasswordForm = this.fb.group(
      {
        newPassword: ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required]],
      },
      {
        validators: this.passwordMatchValidator, // Applichiamo la validazione personalizzata
      }
    );
  }

    // Validazione personalizzata per verificare che il codice fiscale sia in uppercase
    uppercaseValidator(control: AbstractControl): ValidationErrors | null {
      const value = control.value;
      if (value && value !== value.toUpperCase()) {
        return { notUppercase: true }; // Errore se non è tutto in uppercase
      }
      return null;
    }

  // Validazione personalizzata per confrontare le due password
  passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('newPassword')?.value;
    const confermaPassword = group.get('confirmPassword')?.value;
    if (password && confermaPassword && password !== confermaPassword) {
      return { passwordsNonCoincidenti: true }; // Ritorna errore se le password non corrispondono
    }
    return null;
  }

  // Getter per accedere facilmente ai controlli del form
  get emailControl() {
    return this.forgotPasswordForm.get('email')!;
  }

  get fiscalCodeControl() {
    return this.forgotPasswordForm.get('fiscalCode')!;
  }

  get securityAnswerControl() {
    return this.securityQuestionForm.get('securityAnswer')!;
  }

  get newPasswordControl() {
    return this.resetPasswordForm.get('newPassword')!;
  }

  get confirmPasswordControl() {
    return this.resetPasswordForm.get('confirmPassword')!;
  }

  // Funzione per alternare la visibilità della password
  togglePasswordVisibility(field: string): void {
    if (field === 'newPassword') {
      this.isNewPasswordVisible = !this.isNewPasswordVisible;
    } else if (field === 'confirmPassword') {
      this.isConfirmPasswordVisible = !this.isConfirmPasswordVisible;
    }
  }

  // Passa al prossimo step
  goToStep(step: string): void {
    this.currentStep = step;
  }

  // Verifica Email e Codice Fiscale
  submitEmailAndFiscalCode(): void {
    this.email = this.forgotPasswordForm.value.email;
    this.fiscalCode = this.forgotPasswordForm.value.fiscalCode;
    this.userService.getUserQuestion(this.email).subscribe({
      next: (response) => {
        this.utente = response;
        this.domandaPersonale = response.tipoDomanda;
        if (this.forgotPasswordForm.valid && this.utente.codiceFiscale === this.fiscalCode) {
          this.goToStep('securityQuestion');
        } else {
          alert('Compila correttamente tutti i campi!');
        }
      },
      error: (error) => {
        console.error('Login failed:', error);
      },
    });
  }

  // Verifica la risposta alla domanda di sicurezza
  submitSecurityAnswer(): void {
    if (
      this.securityQuestionForm.valid &&
      this.securityQuestionForm.value.securityAnswer === this.utente.rispostaHash
    ) {
      this.goToStep('resetPassword');
    } else {
      alert('Risposta non valida!');
    }
  }

  // Reimposta la password
  resetPassword(): void {
    const newPassword = this.resetPasswordForm.value.newPassword;
    const confirmPassword = this.resetPasswordForm.value.confirmPassword;

    if (this.resetPasswordForm.valid && newPassword === confirmPassword) {
      console.log('RISPOS', this.utente.rispostaHash);
      this.userService.resetPassword(this.email, this.fiscalCode, this.utente.rispostaHash, newPassword).subscribe({
        next: (response) => {
          console.log(response);
        },
        error: (error) => {
          console.error('Login failed:', error);
        },
      });
      this.goToStep('success');
      setTimeout(() => {
        this.router.navigate(['/loggato']); // Naviga verso /loggato dopo 5 secondi
      }, 4000);
    } else {
      alert('Correggi gli errori per continuare!');
    }
  }
}
