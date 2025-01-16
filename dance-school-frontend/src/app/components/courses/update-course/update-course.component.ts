import { Component, Inject, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { CoachService } from '../../../shared/services/coach.service';
import { Coach } from '../../../shared/interface/response-data';
import { CourseService } from '../../../shared/services/course.service';
import { MatDialog } from '@angular/material/dialog';
import { UtilityService } from '../../../shared/services/utility.service';
import { CourseUpdateData } from '../../../shared/interface/request-data';


@Component({
     selector: 'app-update-course',
     standalone: true,
     imports: [CommonModule, ReactiveFormsModule],
     templateUrl: './update-course.component.html',
     styleUrls: ['./update-course.component.css']
})
export class UpdateCourseComponent {
     courseForm: FormGroup;
     coaches: Coach[] = [];
     days: string[] = ['Τρίτη', 'Τετάρτη', 'Πέμπτη', 'Παρασκευή', 'Σάββατο'];
     courseId = ""
     utilityService = inject(UtilityService)

     constructor(
          private fb: FormBuilder,
          public dialogRef: MatDialogRef<UpdateCourseComponent>,
          @Inject(MAT_DIALOG_DATA) public data: CourseUpdateData,  // Τα δεδομένα που περνάμε στο διάλογο
          
          private coachService: CoachService,
          private courseService: CourseService,
     ) {
    // Δημιουργία της φόρμας με τα δεδομένα που περνάμε
          this.courseForm = this.fb.group({
               name: [this.data.name, Validators.required],
               description: [this.data.description, Validators.required],
               day: [this.data.day, Validators.required],
               startTime: [this.data.startTime, Validators.required],
               endTime: [this.data.endTime, Validators.required],
               coachUuid: [this.data.coachUuid, Validators.required]
          });
          this.courseId = this.data.id
     }

     ngOnInit(): void {
          this.loadCoaches();
     }

     loadCoaches(): void {
         
          this.coachService.getAllCoaches().subscribe({
               next: (response) => {
                    this.coaches = response;
               },
               error: (err) => {
                    this.utilityService.openInfoDialog(err.error.description)
                    console.error('Σφάλμα κατά την φόρτωση προπονητών:', err);
               
               }
          });
     }

     onSubmit(): void {
          if (this.courseForm.invalid) {
               alert('Παρακαλώ συμπληρώστε όλα τα υποχρεωτικά πεδία.');
               return;
          }
  
          const updatedCourse = { ...this.courseForm.value, id: this.courseId };
          console.log(updatedCourse);
          
          this.courseService.updateCourse(updatedCourse).subscribe({
               next: () => {
                    this.utilityService.openInfoDialog('Το μάθημα ενημερώθηκε επιτυχώς!');
                    this.courseForm.reset();
                    this.dialogRef.close(updatedCourse)
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
