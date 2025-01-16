import { Component,inject,Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { CoachService } from '../../../shared/services/coach.service'; 
import { Coach } from '../../../shared/interface/response-data';
import { CourseService } from '../../../shared/services/course.service';
import { ReCourse } from '../../../shared/interface/request-data';
import { MatDialog } from '@angular/material/dialog';
import { UtilityService } from '../../../shared/services/utility.service';


@Component({
     selector: 'app-add-course',
     standalone: true,
     imports: [CommonModule,ReactiveFormsModule,MatDialogModule],
     templateUrl: './add-course.component.html',
     styleUrls: ['./add-course.component.css']
})
export class AddCourseComponent {

     coachService = inject(CoachService)
     courseService = inject(CourseService)
     dialog = inject(MatDialog)
     coaches: Coach[]=[];
     days: string[] = ['Τρίτη', 'Τετάρτη', 'Πέμπτη', 'Παρασκευή', 'Σάββατο'];
     utilityService = inject(UtilityService)

     courseForm = new FormGroup({
          name: new FormControl('', Validators.required),
          description: new FormControl('', Validators.required),
          day: new FormControl('', Validators.required),
          startTime: new FormControl('', Validators.required),
          endTime: new FormControl('', Validators.required),
          coachUuid: new FormControl("",Validators.required)
     });

     ngOnInit(): void {
          this.loadCoaches();
     }

     loadCoaches(): void {
          this.coachService.getAllCoaches().subscribe({
               next: (response) => {
                     // Αναθέτουμε τα δεδομένα των προπονητών από την απάντηση στο πεδίο `coaches` της κλάσης
                    // προκειμένου να μπορεί ο χρήστης να επιλέξει έναν προπονητή για να εισάγει ένα μάθημα.
                    this.coaches = response
               },
               error: (err) => {
                    console.error('Σφάλμα κατά την φόρτωση προπονητών:', err);
                    alert('Αποτυχία φόρτωσης προπονητών. Παρακαλώ προσπαθήστε ξανά.');
               }
          });
     }

     onSubmit(){
          if (this.courseForm.valid) {
               const data: ReCourse = this.courseForm.value as ReCourse;
               this.courseService.saveCourse(data).subscribe({
               next: () => {
                    this.utilityService.openInfoDialog('Το μάθημα αποθηκεύτηκε επιτυχώς!')
                    this.courseForm.reset();
               },
               error: (err) => {
                    this.utilityService.openInfoDialog(err.error.description)
                    console.error('Σφάλμα κατά την αποθήκευση:', err);
               }
               });
          } else {
               console.error('Η φόρμα δεν είναι έγκυρη');
          }
     }

}


   



  

  

  

  


