import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators,FormControl, ReactiveFormsModule } from '@angular/forms';
import { CoachService } from '../../../../shared/services/coach.service';
import { CommonModule } from '@angular/common';
import { LoginCredentials, NewCredentials } from '../../../../shared/interface/request-data';
import { UtilityService } from '../../../../shared/services/utility.service';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';


@Component({
     selector: 'app-update-credential',
     standalone: true,
     imports: [CommonModule,ReactiveFormsModule],
     templateUrl: './update-credential.component.html',
     styleUrl: './update-credential.component.css'
})
export class UpdateCredentialComponent {
     coachService = inject(CoachService);
     user = this.coachService.user
     utilityService = inject(UtilityService);
     showNewForm:boolean = false;
     data = inject(MAT_DIALOG_DATA)
     errorMessage = ""

     oldCredentialsForm = new FormGroup({
          password: new FormControl('', [
               Validators.required,
               Validators.pattern("^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[@#$!&?]).{8,}$")
          ]),
     })

     newCredentialsForm = new FormGroup({
          username: new FormControl('', Validators.required),
          password: new FormControl('', [
               Validators.required,
               Validators.pattern("^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[@#$!&?]).{8,}$")
          ]),
     })



     onSubmitOldCredentials() {
          if (this.oldCredentialsForm.valid) {
               const loggedInUser = this.user(); 
          
               if (!loggedInUser || !loggedInUser.username) {
                    this.errorMessage = 'Χρειάζεστε να είστε συνδεδεμένοι με έγκυρο username για να κάνετε αυτήν την ενέργεια.';
                    return;
               }
          
               // Δημιουργούμε το αντικείμενο oldCredentials από τη φόρμα
               const oldCredentials: LoginCredentials = {
                    username: loggedInUser.username,
                    password: this.oldCredentialsForm.value.password || ''
               };
          
               // Κλήση στο service για επαλήθευση των παλιών στοιχείων
               this.coachService.validCredential(oldCredentials).subscribe({
                    next: (response) => {
                         // Αν η απάντηση από το backend περιέχει το "accept", τότε εμφανίζουμε την νέα φόρμα
                         this.showNewForm = response.accept;
                         this.errorMessage = ""
                    },
                    error: (error) => {
                         this.errorMessage = "Λάθος Username/Password";
                         console.error('Σφάλμα κατά την αποθήκευση:', error);
                    }
               });
          }
     }

     onSubmitNewCredentials() {
          if (this.newCredentialsForm.valid) {
     
               const newCredentials: NewCredentials = {
                    username: this.newCredentialsForm.value.username || "", 
                    password: this.newCredentialsForm.value.password || '', 
                    uuid: this.data.uuid 
               };
          
               // Κλήση στο service για αποστολή των νέων credentials
               this.coachService.newCredentials(newCredentials).subscribe({
                    next: (response) => {
                         this.utilityService.openInfoDialog("Τα διαπιστευτήρια ενημερώθηκαν με επιτυχία")
                    },
                    error: (error) => {
                         // Χειρισμός σφαλμάτων
                         this.errorMessage = 'Σφάλμα κατά την ενημέρωση των credentials';
                         console.error('Error updating credentials', error);
                    }
               });
          }
     }
}
