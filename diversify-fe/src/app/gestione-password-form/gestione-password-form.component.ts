import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder, AbstractControl, ValidationErrors } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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

  // Regex per validare l'email (standard)
  private emailPattern: string = '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$';

  // Regex per validare il codice fiscale (italiano - semplificata)
  private fiscalCodePattern: string = '^[A-Za-z0-9]{16}$';

  constructor(private fb: FormBuilder) {
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

  // Validazione personalizzata per confrontare le due password
  passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('newPassword')?.value;
    const confermaPassword = group.get('confirmPassword')?.value;
    if (password && confermaPassword && password !== confermaPassword) {
      return { passwordsNonCoincidenti: true };  // Ritorna errore se le password non corrispondono
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

  // Passa al prossimo step
  goToStep(step: string): void {
    this.currentStep = step;
  }

  // Verifica Email e Codice Fiscale
  submitEmailAndFiscalCode(): void {
    if (this.forgotPasswordForm.valid) {
      console.log('Email e Codice Fiscale validati:', this.email, this.fiscalCode);
      this.goToStep('securityQuestion');
    } else {
      alert('Compila correttamente tutti i campi!');
    }
  }

  // Verifica la risposta alla domanda di sicurezza
  submitSecurityAnswer(): void {
    if (this.securityQuestionForm.valid) {
      console.log('Risposta valida:', this.securityQuestionForm.value.securityAnswer);
      this.goToStep('resetPassword');
    } else {
      alert('Compila la risposta personale!');
    }
  }

  // Reimposta la password
  resetPassword(): void {
    const newPassword = this.resetPasswordForm.value.newPassword;
    const confirmPassword = this.resetPasswordForm.value.confirmPassword;

    if (this.resetPasswordForm.valid && newPassword === confirmPassword) {
      console.log('Nuova password salvata:', newPassword);
      this.goToStep('success');
    } else {
      alert('Correggi gli errori per continuare!');
    }
  }
}