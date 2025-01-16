import { Component, inject } from '@angular/core';
import { MenuEntry } from '../../shared/interface/menu-entry';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CoachService } from '../../shared/services/coach.service';

@Component({
  selector: 'app-menu-list',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './menu-list.component.html',
  styleUrl: './menu-list.component.css'
})
export class MenuListComponent {
     coachService = inject(CoachService)

     menu = [
          {text: "Πρόγραμμα Μαθημάτων", open:false, routerLink: "course-schedule"},
          {text:"Χορευτές",open: false, children:[
               { text:"Διαχείριση Χορευτών",routerLink:"dancers",roles:['Admin', 'Latin_Coach', 'Zumba_Coach', 'HipHop_Coach']},
               { text: 'Εισαγωγή Χορευτή', routerLink: 'add-dancer', roles:['Admin']}
          ]},
          {text:"Μαθήματα",open: false, children:[
               {text: "Διαχείριση Μαθημάτων", routerLink:"courses", roles:['Admin']},
               {text: "Εισαγωγή Μαθήματος", routerLink:"add-course", roles:['Admin']}
          ],roles:['Admin']},
          {text:"Προπονητές",open: false, children:[
               {text: "Διαχείριση Προπονητών", routerLink:"coaches", roles:['Admin', 'Latin_Coach', 'Zumba_Coach', 'HipHop_Coach']},
               {text: "Εισαγωγή Προπονητή", routerLink:"add-coache", roles:['Admin']}
          ]}
     ]

     showMenu(item: any): boolean {
          if (!item.roles || item.roles.length === 0) {
            return true;
          }
      
          return this.coachService.hasAnyRole(item.roles);
     }
}
