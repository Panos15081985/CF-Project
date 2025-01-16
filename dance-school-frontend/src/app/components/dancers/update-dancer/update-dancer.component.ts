import { Component,inject,Inject } from '@angular/core';

import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormControl, FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { Course, Dancer } from '../../../shared/interface/response-data';
import { UtilityService } from '../../../shared/services/utility.service';
import { DancerService } from '../../../shared/services/dancer.service';
import { CourseService } from '../../../shared/services/course.service';



@Component({
  selector: 'app-update-dancer',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule,FormsModule],
  templateUrl: './update-dancer.component.html',
  styleUrl: './update-dancer.component.css'
})
export class UpdateDancerComponent {
     utilityService = inject(UtilityService)
     dancerService = inject(DancerService)
     courseService = inject(CourseService)
     guardianInfo = true
     dancerForm: FormGroup
     courses: Course[] = [];
     oldCourses: Course[] = [];

     constructor(
         private fb: FormBuilder,  // Δηλώνουμε το FormBuilder
         public dialogRef: MatDialogRef<UpdateDancerComponent>,  // Αναφορά στο διάλογο
         @Inject(MAT_DIALOG_DATA) public data: Dancer,
     ) {
        
          this.dancerForm = this.fb.group({
               firstname:[data.firstname,Validators.required],
               lastname: [this.data.lastname,Validators.required],
               dateOfBirth: [this.data.dateOfBirth, Validators.required],
               gender:[this.data.gender,Validators.required],
               contractEnd:[this.data.contractEnd],
               contactDetailsUpdateDTO: this.fb.group({
                    id:[this.data.contactDetailsReadOnlyDTO.id],
                    email: [this.data.contactDetailsReadOnlyDTO.email, Validators.required],
                    phoneNumber:[this.data.contactDetailsReadOnlyDTO.phoneNumber,Validators.required],
                    city:[this.data.contactDetailsReadOnlyDTO.city,Validators.required],
                    road:[this.data.contactDetailsReadOnlyDTO.road],
                    road_number:[this.data.contactDetailsReadOnlyDTO.road_number],
                    postalCode:[this.data.contactDetailsReadOnlyDTO.postalCode]
               }),
               guardianUpdateDTO: !data.guardianReadOnlyDTO ? null : this.fb.group({
                    id:[this.data.guardianReadOnlyDTO?.id],
                    firstname:[this.data.guardianReadOnlyDTO?.firstname,Validators.required],
                    lastname:[this.data.guardianReadOnlyDTO?.lastname,Validators.required]
               }),
               courseIds: new FormControl<number[]>([]),
               uuid:[this.data.uuid]

          });
          this.oldCourses = data.courseList
          this.courseService.getAllCourses().subscribe({
               next: (courses) => {
                 this.courses = courses;
               },
               error: (err) => {
                 console.error('Error fetching courses:', err);
               }
          });
          if (!this.data.guardianReadOnlyDTO) {
               
               this.dancerForm.get('guardianInsertDTO')?.disable();
               this.guardianInfo = false;
          }
     }

     get hasNoSelectedCourses(): boolean {
          const courseIds = this.dancerForm.get('courseIds')?.value;
          return !courseIds || courseIds.length === 0;
     }

     onSubmit(){
          this.dancerService.updateDancer(this.dancerForm.value).subscribe({
               next: (response) =>{
                    this.utilityService.openInfoDialog('Ο Χορευτής ενημερώθηκε επιτυχώς!');
                    this.dancerForm.reset();
                    this.dialogRef.close(response)
               },
               error: (err) => {
                    this.utilityService.openInfoDialog(err.error.description)
                    console.log(err)
               }
          })
     }

     onCancel(){
          this.dialogRef.close(); 
     }
}
