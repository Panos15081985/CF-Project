import { Component, inject } from '@angular/core';
import { CourseService } from '../../../shared/services/course.service';
import { Course, Dancer } from '../../../shared/interface/response-data';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CoachService } from '../../../shared/services/coach.service';
import { CommonModule } from '@angular/common';
import { DancerService } from '../../../shared/services/dancer.service';
import { UtilityService } from '../../../shared/services/utility.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';
import { UpdateDancerComponent } from '../update-dancer/update-dancer.component';

@Component({
     selector: 'app-dancers',
     standalone: true,
     imports: [ReactiveFormsModule,CommonModule],
     templateUrl: './dancers.component.html',
     styleUrl: './dancers.component.css'
})
export class DancersComponent {
     courseService = inject(CourseService);
     coachService = inject(CoachService)
     dancerService = inject(DancerService)
     dialog = inject(MatDialog)
     courses: Course[] = [];
     filteredDancers: Dancer[] = [];
     userUuid = this.coachService.user()?.uuid
     userRole =this.coachService.user()?.role
     expandedCoach: number | null = null;
     utilityService = inject(UtilityService)

     page: number = 0; // Τρέχουσα σελίδα // Μέγεθος σελίδας (πόσα δεδομένα ανα σελίδα)
     totalPages: number = 1; // Συνολικός αριθμός σελίδων
     totalElements: number = 0; // Συνολικός αριθμός στοιχείων (από το API)


     filtersForm = new FormGroup({
          coachUuid: new FormControl(this.userRole === "Admin" ? "" : this.userUuid),
          courseId: new FormControl(""),
          lastname: new FormControl(""),
          dateOfBirth: new FormControl(""),
          contract_end_from: new FormControl(""),
          contract_end_until: new FormControl(""),
          isUnder18: new FormControl (null),
     })

     ngOnInit(){
         this.loadCourses()
     }

     loadCourses(): void {
          this.courseService.getAllCourses().subscribe({
               next: (response) => {
                // Ελέγχει αν ο χρήστης είναι Admin ή άλλος ρόλος
               // Αν ο χρήστης είναι Admin, φορτώνονται όλα τα μαθήματα, αλλιώς φιλτράρονται τα μαθήματα με βάση το coachUuid.
               this.courses = this.userRole === "Admin" ? response : this.sortedCourses(response)
               },
               error: (err) => {
                    this.utilityService.openInfoDialog(err.error.description)
               console.error('Error fetching courses:', err);
               
               }
          });
     }

     sortedCourses(data: Course[]): Course[]{
          return data.filter(course => course.coachUuid === this.userUuid);
     }

     onSubmit(){
          // Παίρνει την τιμή του πεδίου 'isUnder18' από τη φόρμα, αν δεν υπάρχει τιμή, χρησιμοποιεί το null
          const isUnder18Value = this.filtersForm.get("isUnder18")?.value ?? null;
          const filters = {
            ...this.filtersForm.value,
            page: this.page,
            isUnder18: isUnder18Value === false ? null : isUnder18Value,
          };
        
          this.dancerService.getFilteredDancers(filters).subscribe({
               next:(response) =>{
                    this.filteredDancers = response.data
                    this.totalPages = response.totalPages
               },
               error:(err) =>{
                    this.utilityService.openInfoDialog(err.error.description)
                    console.log(err)
               }
          })
     }

     goToPage(pageNumber: number) {
          if (pageNumber >= 0 && pageNumber < this.totalPages) {
               this.page = pageNumber;
               this.onSubmit(); 
          }
     }

     deleteDancer(uuid: string, i:number): void {
         // Ανοίγουμε τον διάλογο επιβεβαίωσης
         const dialogRef = this.dialog.open(ConfirmDialogComponent, {
           data: {
             message: 'Είσαι σίγουρος/η ότι θέλεις να διαγράψεις αυτό τον Χορευτή?',
             messageType: 'confirmation'
           },
           panelClass: 'custom-dialog-container' // Χρήση προσαρμοσμένης κλάσης
         });
       
          dialogRef.afterClosed().subscribe(result => {
               if (result) {
               // Αν η επιβεβαίωση ήταν θετική, προχωράμε με τη διαγραφή
                    this.dancerService.deleteDancer(uuid).subscribe({
                         next: (response) => {
                         this.utilityService.openInfoDialog('Ο Χορευτής διαγράφηκε');
                         this.filteredDancers.splice(i, 1);
                         },
                         error: (err) => {
                         if (err.status == "409") {
                         this.utilityService.openInfoDialog(err.error.description);
                         }
                         }
                    });
               }
          });
     }

     editDancer(index:number){
          const editData = this.filteredDancers.find((dancer, i) => i === index);
          const dialogRef = this.dialog.open(UpdateDancerComponent, {
               data: editData,
               panelClass: 'custom-dialog-container',
               maxHeight: "80vh"
          });
     
          dialogRef.afterClosed().subscribe(result => {
               if (result) {
                    this.filteredDancers[index] = { ...this.filteredDancers[index], ...result };
               
               }
          });
     }

     toggleExpand(index: number): void {
          this.expandedCoach = this.expandedCoach === index ? null : index;
     }

}
