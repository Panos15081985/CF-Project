import { Component, inject } from '@angular/core';
import { FormControl,FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CoachService } from '../../../shared/services/coach.service';
import { CommonModule } from '@angular/common';
import { ReCoach } from '../../../shared/interface/request-data';
import { MatDialog } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { UtilityService } from '../../../shared/services/utility.service';

@Component({
     selector: 'app-add-coach',
     standalone: true,
     imports: [ReactiveFormsModule,CommonModule,MatDialogModule],
     templateUrl: './add-coach.component.html',
     styleUrl: './add-coach.component.css'
})
export class AddCoachComponent {
     roles = ['HipHop_Coach', 'Latin_Coach', 'Zumba_Coach'];
     coachService = inject(CoachService);
     dialog = inject(MatDialog)
     utilityService = inject(UtilityService)

     // Δημιουργία φόρμας για την καταχώρηση προπονητή
     coachForm = new FormGroup({
          firstname: new FormControl('', Validators.required),
          lastname: new FormControl('', Validators.required),
          username: new FormControl('', Validators.required),
          password: new FormControl('', [
               Validators.required,
               Validators.pattern("^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[@#$!&?]).{8,}$")
          ]),
          role: new FormControl('', Validators.required),
          contactDetailsInsertDTO: new FormGroup({
               city: new FormControl('', Validators.required),
               road_number: new FormControl(''),
               road: new FormControl(''),
               postalCode: new FormControl(''),
               email: new FormControl('', [Validators.required, Validators.email]),
               phoneNumber: new FormControl('', [Validators.required, Validators.pattern('^[0-9]+$')])
          })
     });


     onSubmit() {
          if (this.coachForm.valid) {
               const coach: ReCoach = this.coachForm.value as ReCoach
               this.coachService.saveCoach(coach).subscribe({
               next: () => {
                    this.utilityService.openInfoDialog('Ο προπονητής αποθηκεύτηκε επιτυχώς!');
                    this.coachForm.reset();
               },
               error: (err) => {
                    this.utilityService.openInfoDialog(err.error.description);
                    console.error('Σφάλμα κατά την αποθήκευση:', err);
               }
               });
          }
     }
}
