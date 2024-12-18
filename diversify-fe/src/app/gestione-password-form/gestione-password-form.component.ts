import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-gestione-password-form',
  standalone: true, 
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './gestione-password-form.component.html',
  styleUrls: ['./gestione-password-form.component.css']
})
export class GestionePasswordFormComponent {
  currentStep: string = 'forgotPassword'; // Step iniziale
  forgotPasswordForm: FormGroup; // Dichiarazione del FormGroup
  
  email: string = '';
  fiscalCode: string = '';
  securityAnswer: string = '';
  newPassword: string = '';
  confirmPassword: string = '';

  // Regex per validare la password
  private passwordPattern: string = '^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]+$';
  
  // Regex per validare l'email (standard)
  private emailPattern: string = '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$';

  // Regex per validare il codice fiscale (italiano - semplificata)
  private fiscalCodePattern: string = '^[A-Za-z0-9]{16}$';  // Formato generico, 16 caratteri
  
  constructor(private fb: FormBuilder) {
    // Inizializzazione del form
    this.forgotPasswordForm = this.fb.group({
      email: [
        '',
        [
          Validators.required,
          Validators.pattern(this.emailPattern) // Regex per email
        ],
      ],
      fiscalCode: [
        '',
        [
          Validators.required,
          Validators.pattern(this.fiscalCodePattern) // Regex per codice fiscale
        ],
      ],
    });
  }

  // Getter per accedere facilmente ai controlli del form
  get emailControl() {
    return this.forgotPasswordForm.get('email')!;
  }

  get fiscalCodeControl() {
    return this.forgotPasswordForm.get('fiscalCode')!;
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
    if (this.securityAnswer === 'animale') { // Simulazione risposta corretta
      console.log('Risposta valida:', this.securityAnswer);
      this.goToStep('resetPassword');
    } else {
      alert('Risposta errata!');
    }
  }

  // Reimposta la password
  resetPassword(): void {
    if (this.newPassword === this.confirmPassword && this.newPassword.length >= 6) {
      console.log('Nuova password salvata:', this.newPassword);
      this.goToStep('success');
    } else {
      alert('Le password non coincidono o non sono valide!');
    }
  }
}
