import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from '../login/login.component';
import { CoachService } from '../../shared/services/coach.service';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

     dialog = inject(MatDialog)
     coachService = inject(CoachService);
     username: string | undefined;
     isLoggedIn: boolean = false;
     user = this.coachService.user()?.username

     ngOnInit(): void {
          this.checkLoginStatus();
     }

     checkLoginStatus(): void {
          const user = this.coachService.user();
          if (user?.username) {
               this.username = user.username;
               this.isLoggedIn = true;
          } else {
               this.isLoggedIn = false;
               this.username = undefined;
          }
     }

     logout(): void {
          this.coachService.logoutUser();
          this.isLoggedIn = false;
          this.username = undefined;
     }

     login(){
          const dialogRef = this.dialog.open(LoginComponent, {
               width: "400px",
               height:"50vh",
               maxHeight: "50vh",
               maxWidth:"400px"
          });
          dialogRef.afterClosed().subscribe(result => {
               if (result) {
                    this.isLoggedIn = true
                    this.username = result;
               }
          });
     }
}


  

 


 



  



