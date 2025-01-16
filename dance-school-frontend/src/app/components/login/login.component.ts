import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { LoggedInUser, LoginCredentials, TokenPayload } from '../../shared/interface/request-data';
import { MatDialogRef } from '@angular/material/dialog';
import { CoachService } from '../../shared/services/coach.service';
import { Router} from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule,FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {

     coachService = inject(CoachService);
     router = inject(Router);
     loginForm: FormGroup; // Διόρθωση: Ορισμός του loginForm
     errorText = ""

     constructor(
          private fb: FormBuilder, 
          public dialogRef: MatDialogRef<LoginComponent>, 
     ){
          this.loginForm = this.fb.group({
               username: ['', Validators.required],
               password: ['',
               [
                    Validators.required,
                    Validators.pattern(
                    '^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[@#$!&?]).{8,}$'
                    ),
               ]]
          });
     }

     onSubmit() {
          if (this.loginForm.invalid) {
               console.log('Form is invalid');
               return;
          }

          localStorage.removeItem('access_token');
          const credentials = this.loginForm.value as LoginCredentials;

          this.coachService.loginUser(credentials).subscribe({
               next: (response) => {
                    const token = response.token;
                    localStorage.setItem('access_token', token);

                    const decodeTokenSubject = jwtDecode(token) as TokenPayload;
     
                    this.coachService.user.set({
                         username: decodeTokenSubject.sub,
                         role: decodeTokenSubject.role,
                         uuid: decodeTokenSubject.uuid,
                    });
                    this.dialogRef.close(this.loginForm.get("username")?.value); // Κλείσιμο του διαλόγου
               },
               error: (error) => {
                    if(error.error.code == "userNotAuthenticated"){
                         this.errorText = "Λάθος Όνομα Χρήστη ή Κωδικού"
                    }
               },
          });
     }
}