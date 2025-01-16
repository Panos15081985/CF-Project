import { Component, inject } from '@angular/core';
import { CourseService } from '../../shared/services/course.service';
import { CommonModule } from '@angular/common';
import { Course } from '../../shared/interface/response-data';
import { UtilityService } from '../../shared/services/utility.service';

@Component({
     selector: 'app-course-schedule',
     standalone: true,
     imports: [CommonModule],
     templateUrl: './course-schedule.component.html',
     styleUrl: './course-schedule.component.css'
})
export class CourseScheduleComponent {
     courseService = inject(CourseService)
     utilityService = inject(UtilityService)
     sortedCourses: Course[] = []

     ngOnInit(): void {
          this.loadCourses();
     }

     loadCourses(): void {
          this.courseService.getAllCourses().subscribe({
               next: (response) => {
                    this.sortedCourses = this.sortCoursesByDay(response)
               },
               error: (err) => {
                    console.error("Error fetching courses:", err);
                    this.utilityService.openInfoDialog(err.error.description)
               },
          });
     }

     sortCoursesByDay(courses: Course[]): Course[] {
     
          const daysMap: { [key: string]: number } = {
               'Δευτέρα': 0,
               'Τρίτη': 1,
               'Τετάρτη': 2,
               'Πέμπτη': 3,
               'Παρασκευή': 4,
               'Σάββατο': 5,
               'Κύριακή': 6
          };

          return courses.sort((a, b) => {
               // Προσθήκη ελέγχου για να διασφαλίσουμε ότι οι ημέρες υπάρχουν στο daysMap
               const dayAIndex = daysMap[a.day];
               const dayBIndex = daysMap[b.day];

               if (dayAIndex === undefined || dayBIndex === undefined) {
                    console.error('Invalid day value:', a.day, b.day);
                    return 0; 
               }

               // Ταξινόμηση πρώτα με βάση την ημέρα
               if (dayAIndex !== dayBIndex) {
                    return dayAIndex - dayBIndex;
               }

               // Αν οι ημέρες είναι ίδιες, ταξινόμηση με βάση την ώρα
               const timeA = this.parseTime(a.startTime);
               const timeB = this.parseTime(b.startTime);
               return timeA - timeB;
          });
     }

     parseTime(time: string): number {
          if (!time) return 0;
          const [hour, minute] = time.split(':').map(Number);
          return hour * 60 + minute; // Μετατροπή της ώρας σε λεπτά
     }
}
