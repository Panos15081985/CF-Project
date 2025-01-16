import { Component, inject } from '@angular/core';
import { Coach, CoachUuid, Course } from '../../../shared/interface/response-data';
import { CourseService } from '../../../shared/services/course.service';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';
import { UpdateCourseComponent } from '../update-course/update-course.component';
import { UtilityService } from '../../../shared/services/utility.service';
import { CoachService } from '../../../shared/services/coach.service';

@Component({
     selector: 'app-courses',
     standalone: true,
     imports: [FormsModule, CommonModule, ReactiveFormsModule],
     templateUrl: './courses.component.html',
     styleUrls: ['./courses.component.css']
})
export class CoursesComponent {
  
     sortedCourses: Course[] = [];
     filteredCourses: Course[] = [];
     isEditing: boolean = false;
     utilityService = inject(UtilityService)
     coachService= inject(CoachService)
     courseService = inject(CourseService)
     dialog = inject(MatDialog)
     page: number = 0; // Τρέχουσα σελίδα // Μέγεθος σελίδας (πόσα δεδομένα ανα σελίδα)
     totalPages: number = 1; // Συνολικός αριθμός σελίδων
     totalElements: number = 0; // Συνολικός αριθμός στοιχείων (από το API)
     uniqueDays: string[] = ["Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή", 'Σάββατο']
     uniqueCoaches: Coach[] = []

     filtersForm = new FormGroup({
          day: new FormControl(""),
          name: new FormControl(""),
          coachId: new FormControl("")
     })


     ngOnInit(): void {this.loadCoaches();}

     loadCoaches(){
          this.coachService.getAllCoaches().subscribe({
               next:(response) =>{
                    this.uniqueCoaches = response 
               },
               error:(error) => {
                    console.log(error)
               }
          })
     }

     onSubmit(): void {
          const filters = {...this.filtersForm.value, page: this.page}
         
          this.courseService.getFilteredCourses(filters).subscribe({
               next: (response) => {
                    this.sortedCourses = this.sortCoursesByDay(response.data);
                    this.totalPages = response.totalPages;
                    this.totalElements = response.totalElements;
               },
               error: (err) => {
                    console.error('Error fetching courses:', err);
                    this.utilityService.openInfoDialog(err.error.description)
               }
          });
     }

     sortCoursesByDay(courses: Course[]): Course[] {
          const dayOrder = ['Δευτέρα', 'Τρίτη', 'Τετάρτη', 'Πέμπτη', 'Παρασκευή', 'Σάββατο', 'Κυριακή'];
          
          return courses.sort((a, b) => {
               const dayA = dayOrder.indexOf(a.day);
               const dayB = dayOrder.indexOf(b.day);
          
               if (dayA !== dayB) {
               return dayA - dayB;
               }
               const timeA = this.parseTime(a.startTime);
               const timeB = this.parseTime(b.endTime);
               return timeA - timeB;
          });
     }

     parseTime(time: string): number {
          const [start] = time.split('-');
          const [hour, minute] = start.split(':').map(Number);
          return hour * 60 + minute;
     }

     goToPage(pageNumber: number) {
          if (pageNumber >= 0 && pageNumber < this.totalPages) {
            this.page = pageNumber;
            this.onSubmit(); // Επανεκκίνηση της αναζήτησης με τη νέα σελίδα
          }
     }

     deleteCourse(index: number, i:number): void {
          // Ανοίγουμε τον διάλογο επιβεβαίωσης
          const dialogRef = this.dialog.open(ConfirmDialogComponent, {
               data: {
               message: 'Είσαι σίγουρος/η ότι θέλεις να διαγράψεις αυτό το μάθημα?',
               messageType: 'confirmation'
               },
               panelClass: 'custom-dialog-container' // Χρήση προσαρμοσμένης κλάσης
          });
     
          dialogRef.afterClosed().subscribe(result => {
               if (result) {
                    // Αν η επιβεβαίωση ήταν θετική, προχωράμε με τη διαγραφή
                    this.courseService.deleteCourse(index).subscribe({
                         next: (response) => {
                              this.utilityService.openInfoDialog(response.message);
                              this.sortedCourses.splice(i, 1);
                         },
                         error: (err) => {
                              console.log(err)
                              this.utilityService.openInfoDialog(err.error.description);
                         }
                    });
               }
          });
     }
     
     editCourse(index: number): void {
          const editData = this.sortedCourses.find((course, i) => i === index);
          const dialogRef = this.dialog.open(UpdateCourseComponent, {
               data: editData,
               panelClass: 'custom-dialog-container' // Χρήση προσαρμοσμένης κλάσης
          });

          dialogRef.afterClosed().subscribe(result => {
               if (result) {
                    this.sortedCourses[index] = { ...this.sortedCourses[index], ...result };
               }
          });
     }
}
