import { Component, inject } from '@angular/core';
import { CoachService } from '../../../shared/services/coach.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';
import { UtilityService } from '../../../shared/services/utility.service';
import { UpdateCoachMenuComponent } from '../update-coach/update-coach-menu/update-coach-menu.component';
import { Coach } from '../../../shared/interface/response-data';
import { CommonModule } from '@angular/common';
import { ContactDetails } from '../../../shared/interface/data';

@Component({
     selector: 'app-coaches',
     standalone: true,
     imports: [CommonModule],
     templateUrl: './coaches.component.html',
     styleUrl: './coaches.component.css'
})
export class CoachesComponent {
     coachService = inject(CoachService);
     userUuid = this.coachService.user()?.uuid
     userRole =this.coachService.user()?.role
     filteredCoaches: Coach[] = [];
     expandedCoach: number | null = null;
     dialog = inject(MatDialog)
     utilityService = inject(UtilityService)
     page: number = 0;
     totalPages: number = 1;

     ngOnInit(): void {
          this.loadCoaches();
     }

     loadCoaches(): void {
          const filters = {page: this.page,uuid:this.userRole === "Admin" ? "" : this.userUuid};
          this.coachService.getFilteredCoaches(filters).subscribe({
               next: (response) => {
                    this.filteredCoaches = response.data;
                    this.totalPages = response.totalPages
                    
               },
               error: (err) => {
                    console.error('Error fetching courses:', err);
                    this.utilityService.openInfoDialog(err.error.description)
               }
          });
     }

     deleteCourse(uuid: string, i:number): void {
          // Ανοίγουμε τον διάλογο επιβεβαίωσης
          const dialogRef = this.dialog.open(ConfirmDialogComponent, {
               data: {
                    message: 'Είσαι σίγουρος/η ότι θέλεις να διαγράψεις αυτό τον Προπονητή?',
                    messageType: 'confirmation'
               },
               panelClass: 'custom-dialog-container'
          });
     
          dialogRef.afterClosed().subscribe(result => {
               if (result) {
                    // Αν η επιβεβαίωση ήταν θετική, προχωράμε με τη διαγραφή
                    this.coachService.deleteCoach(uuid).subscribe({
                         next: (response) => {
                              this.utilityService.openInfoDialog('Ο Προπονητής διαγράφηκε');
                              this.filteredCoaches.splice(i, 1);
                         },
                         error: (err) => {
                              this.utilityService.openInfoDialog(err.error.description);
                         }
                    });
               }
          });
     }

     // Μέθοδος για την επεξεργασία των στοιχείων ενός προπονητή.
     editCourse(index: number): void {

          const editData = this.filteredCoaches.find((coach, i) => i === index);
          const dialogRef = this.dialog.open(UpdateCoachMenuComponent, {
               data: editData,
               panelClass: 'custom-dialog-container',
               maxHeight: "80vh"
          });

          dialogRef.afterClosed().subscribe(result => {
               if (result) {
                    this.filteredCoaches[index] = { ...this.filteredCoaches[index], ...result };
               }
          });
     }

     // Μέθοδος για την επέκταση ή συρρίκνωση των πληροφοριών ενός προπονητή.
     toggleExpand(index: number): void {
          this.expandedCoach = this.expandedCoach === index ? null : index;
     }

     // Μέθοδος για την πλοήγηση στις σελίδες των προπονητών.
     goToPage(pageNumber: number) {
          if (pageNumber >= 0 && pageNumber < this.totalPages) {
               this.page = pageNumber;
               this.loadCoaches(); // Επανεκκίνηση της αναζήτησης με τη νέα σελίδα
          }
     }
}


