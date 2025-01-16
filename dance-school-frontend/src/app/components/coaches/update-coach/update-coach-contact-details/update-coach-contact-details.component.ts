import { UtilityService } from '../../../../shared/services/utility.service';
import { Component, Inject, inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormControl, FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { CoachService } from '../../../../shared/services/coach.service';
import { Coach } from '../../../../shared/interface/response-data';

@Component({
  selector: 'app-update-coach-contact-details',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule,FormsModule],
  templateUrl: './update-coach-contact-details.component.html',
  styleUrl: './update-coach-contact-details.component.css'
})
export class UpdateCoachContactDetailsComponent {

     coachService = inject(CoachService)
     utilityService = inject(UtilityService)
     userRole = this.coachService.user()?.role
     coachForm: FormGroup;
     uuid = "";

     constructor(
          private fb: FormBuilder, 
          public dialogRef: MatDialogRef<UpdateCoachContactDetailsComponent>,  // Αναφορά στο διάλογο
          @Inject(MAT_DIALOG_DATA) public data: Coach,
               // Τα δεδομένα που περνάμε στο διάλογο
     ) {
          // Δημιουργία της φόρμας με τα δεδομένα που περνάμε
          this.coachForm = this.fb.group({
               firstname: [this.data.firstname, Validators.required],
               lastname: [this.data.lastname,Validators.required],
               role: [this.data.role, Validators.required],
               contactDetailsUpdateDTO: this.fb.group({
                    id:[this.data.contactDetailsReadOnlyDTO.id],
                    email: [this.data.contactDetailsReadOnlyDTO.email, Validators.required],
                    phoneNumber:[this.data.contactDetailsReadOnlyDTO.phoneNumber,Validators.required],
                    city:[this.data.contactDetailsReadOnlyDTO.city,Validators.required],
                    road:[this.data.contactDetailsReadOnlyDTO.road],
                    road_number:[this.data.contactDetailsReadOnlyDTO.road_number],
                    postalCode:[this.data.contactDetailsReadOnlyDTO.postalCode]
               })
          });
          this.uuid = this.data.uuid
     }

     onSubmit(): void {
          if (this.coachForm.invalid) {
               alert('Παρακαλώ συμπληρώστε όλα τα υποχρεωτικά πεδία.');
               return;
          }
          const updatedCoach = { ...this.coachForm.value, uuid: this.uuid };
          
          this.coachService.updateCoach(updatedCoach).subscribe({
               next: (response) => {
                    this.utilityService.openInfoDialog('Ο Προπονητής ενημερώθηκε επιτυχώς!');
                    this.coachForm.reset();
                    this.dialogRef.close(response)
               },
               error: (err: any) => {
                    this.utilityService.openInfoDialog(err.error.description);
                    console.error('Σφάλμα κατά την αποθήκευση:', err);
               }
          });
     }

     onCancel(): void {
          this.dialogRef.close(); // Κλείνει το διάλογο
     }

}
