import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormControl, Validators, FormGroup, ReactiveFormsModule, FormArray, FormsModule } from '@angular/forms';
import { Course } from '../../../shared/interface/response-data';
import { CourseService } from '../../../shared/services/course.service';
import { ReDancer } from '../../../shared/interface/request-data';
import { DancerService } from '../../../shared/services/dancer.service';
import { UtilityService } from '../../../shared/services/utility.service';


@Component({
     selector: 'app-add-dancer',
     standalone: true,
     imports: [CommonModule,ReactiveFormsModule,FormsModule],
     templateUrl: './add-dancer.component.html',
     styleUrl: './add-dancer.component.css'
})
export class AddDancerComponent {

     isMinor: boolean = false;
     courses: Course[] = [];
     courseService = inject(CourseService)
     dancerService = inject(DancerService);
     utilityService = inject(UtilityService)

     ngOnInit(): void {
          this.loadCourses();
     }
    
     dancerForm = new FormGroup({
          firstname: new FormControl('', Validators.required),
          lastname: new FormControl('', Validators.required),
          dateOfBirth: new FormControl("", Validators.required),
          gender: new FormControl("",Validators.required),
          contactDetailsInsertDTO: new FormGroup({
               city: new FormControl('', Validators.required),
               road_number: new FormControl(''),
               road: new FormControl(''),
               postalCode: new FormControl(''),
               email: new FormControl('', [Validators.required, Validators.email]),
               phoneNumber: new FormControl('', [Validators.required, Validators.pattern('^[0-9]+$')])
     }),
          guardianInsertDTO: new FormGroup({
          firstname: new FormControl("",Validators.required),
          lastname: new FormControl("",Validators.required)
     }),
          courseIds: new FormControl<number[]>([],Validators.required)
     });

     loadCourses(): void {
          this.courseService.getAllCourses().subscribe({
               next: (response) => {
                    this.courses = response
               },
               error: (err) => {
                    this.utilityService.openInfoDialog(err.error.description)
                    console.error('Error fetching courses:', err);
                    
               }
          });
     }

     //Ελέγχει αν ο χορευτής είναι ανήλικος ή όχι, βασιζόμενο στην ημερομηνία γέννησής του.
     //Αν ο χορευτής είναι ανήλικος, ενεργοποιεί τα πεδία για τα στοιχεία του κηδεμόνα. 
     checkAge() {
          const dateOfBirth = this.dancerForm.get("dateOfBirth")?.value;
          if (dateOfBirth) {
               const BirthDay = new Date(dateOfBirth); // Δημιουργούμε την ημερομηνία αν υπάρχει τιμή
               const today = new Date();
               const age = today.getFullYear() - BirthDay.getFullYear();
               this.isMinor = age < 18;
               if (this.isMinor) {
                    this.dancerForm.get('guardianInsertDTO')?.enable();
               } else {
                    this.dancerForm.get('guardianInsertDTO')?.disable();
               }
          }
     }

     onSubmit() {
          if (this.dancerForm.valid) {
               const dancer: ReDancer = this.dancerForm.value as ReDancer
               this.dancerService.savedancer(dancer).subscribe({
                    next: () => {
                         this.utilityService.openInfoDialog('Ο Χορευτής αποθηκεύτηκε επιτυχώς!');
                         this.dancerForm.reset();
                    },
                    error: (err) => {
                         this.utilityService.openInfoDialog(err.error.description);
                         console.error('Σφάλμα κατά την αποθήκευση:', err);
                    }
               });
          }
     }
}
 

