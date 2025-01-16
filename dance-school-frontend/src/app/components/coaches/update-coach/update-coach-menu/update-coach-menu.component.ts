import { Component } from '@angular/core';
import { UpdateCoachContactDetailsComponent } from '../update-coach-contact-details/update-coach-contact-details.component';
import { CommonModule } from '@angular/common';
import { UpdateCredentialComponent } from '../update-credential/update-credential.component';
import {
  trigger,
  transition,
  style,
  animate,
} from '@angular/animations';

@Component({
     selector: 'app-update-coach-menu',
     standalone: true,
     imports: [CommonModule,UpdateCoachContactDetailsComponent,UpdateCredentialComponent],
     templateUrl: './update-coach-menu.component.html',
     styleUrl: './update-coach-menu.component.css',
     animations: [
          trigger('slideToggle', [
               transition(':enter', [
               style({ height: '0', opacity: 0 }),
               animate('300ms ease-out', style({ height: '*', opacity: 1 })),
               ]),
               transition(':leave', [
               style({ height: '*', opacity: 1 }),
               animate('300ms ease-in', style({ height: '0', opacity: 0 })),
               ]),
          ]),
     ],
})
export class UpdateCoachMenuComponent {

     showContactDetails = false;
     showCredentials = false;

      /**
     * Μέθοδος που εναλλάσσει την ορατότητα των φορμών επεξεργασίας.
     * Εξαρτάται από το όνομα της φόρμας που δίνεται ως παράμετρος (contactDetails ή credentials).
     * Κλείνει τη φόρμα που δεν είναι επιλεγμένη και ανοίγει την αντίστοιχη φόρμα.
     * 
     * @param form - Όνομα της φόρμας ('contactDetails' ή 'credentials') για την οποία θα γίνει η αλλαγή.
     */
     toggleForm(form: string): void {
          if (form === 'contactDetails') {
               this.showContactDetails = !this.showContactDetails;
               this.showCredentials = false; 
          } else if (form === 'credentials') {
               this.showCredentials = !this.showCredentials;
               this.showContactDetails = false;
          }
     }
}
