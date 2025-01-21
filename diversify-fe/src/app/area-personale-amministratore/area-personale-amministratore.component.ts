import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';  // Already imported for form
import { CommonModule } from '@angular/common';  // Import CommonModule for *ngIf and other common directives
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-area-personale-amministratore',
  templateUrl: './area-personale-amministratore.component.html',
  styleUrls: ['./area-personale-amministratore.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule]  // Include CommonModule here
})
export class AreaPersonaleAmministratoreComponent {
  modificaPassword: boolean = false;
  admin: any;
  token:any;
  isVecchiaPasswordVisible = false;
  isPasswordVisible = false;
  isConfermaPasswordVisible = false;

  formModificaPassword: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private userService: UserService) {
    this.formModificaPassword = this.fb.group({
      vecchiaPassword: ['', [Validators.required, Validators.minLength(8)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confermaPassword: ['', Validators.required],
    }, { validators: this.passwordsMustMatch });
  }

  ngOnInit() {
    this.token = this.authService.getToken();
    if (this.token) {
      this.userService.getUserFromToken(this.token).subscribe(
          (data) => {
          this.admin = data;
          },
          (error) => {
          console.error('Errore nel recupero dei dati utente:', error);
          }
      );
      } else {
      console.error('Token non trovato.');
      }
  }

  passwordsMustMatch(group: FormGroup): { [key: string]: boolean } | null {
    const password = group.get('password')?.value;
    const confermaPassword = group.get('confermaPassword')?.value;
    return password && confermaPassword && password !== confermaPassword ? { 'passwordsNonCoincidenti': true } : null;
  }

  toggleModificaPassword(): void {
    this.modificaPassword = !this.modificaPassword;
  }

  togglePasswordVisibility(field: string): void {
    if (field === 'vecchiaPassword') {
      this.isVecchiaPasswordVisible = !this.isVecchiaPasswordVisible;
    } else if (field === 'password') {
      this.isPasswordVisible = !this.isPasswordVisible;
    } else if (field === 'confermaPassword') {
      this.isConfermaPasswordVisible = !this.isConfermaPasswordVisible;
    } 
  }

  salvaModificaPassword(): void {
    if (this.formModificaPassword.valid && this.token) {
      const vecchiaPassword = this.formModificaPassword.get('vecchiaPassword')?.value;
      const nuovaPassword = this.formModificaPassword.get('password')?.value;
      this.userService.changePassword(this.token, vecchiaPassword, nuovaPassword).subscribe({
          next: (response) => {
            alert(response.message);
            this.toggleModificaPassword();
          },
          error: (error) => {
            alert(error);
          }
        });
  } else {
      console.log('Errore: le password non corrispondono o sono incomplete');
  }
  }
}