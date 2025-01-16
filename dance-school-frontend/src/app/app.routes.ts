import { Routes } from '@angular/router';
import { CoachesComponent } from './components/coaches/coaches/coaches.component';
import { AddCoachComponent } from './components/coaches/add-coach/add-coach.component';
import { UpdateCoachContactDetailsComponent } from './components/coaches/update-coach/update-coach-contact-details/update-coach-contact-details.component';
import { UpdateCredentialComponent } from './components/coaches/update-coach/update-credential/update-credential.component';
import { CoursesComponent } from './components/courses/courses/courses.component';
import { AddCourseComponent } from './components/courses/add-course/add-course.component';
import { UpdateCourseComponent } from './components/courses/update-course/update-course.component';
import { DancersComponent } from './components/dancers/dancers/dancers.component';
import { AddDancerComponent } from './components/dancers/add-dancer/add-dancer.component';
import { UpdateDancerComponent } from './components/dancers/update-dancer/update-dancer.component';
import { CourseScheduleComponent } from './components/course-schedule/course-schedule.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { authGuard } from './shared/guards/auth.guard';

export const routes: Routes = [
     {path: '', component: WelcomeComponent,},
     {path:"coaches", component: CoachesComponent, canActivate: [authGuard], data: { roles: 
          ['Admin','HipHop_Coach','Latin_Coach','Zumba_Coach'] } },
     {path:"add-coache", component: AddCoachComponent, canActivate: [authGuard], data: { roles: ['Admin'] } },
     {path:"update-coach-Contact-details", component: UpdateCoachContactDetailsComponent,  canActivate: [authGuard], data: {
          roles: ['Admin','HipHop_Coach','Latin_Coach','Zumba_Coach']}},
     {path:"update-coach-credentials", component: UpdateCredentialComponent,  canActivate: [authGuard], data: {
          roles: ['Admin','HipHop_Coach','Latin_Coach','Zumba_Coach']}},
     {path:"courses", component: CoursesComponent,  canActivate: [authGuard], data: { roles: ['Admin'] } },
     {path:"add-course", component: AddCourseComponent, canActivate: [authGuard], data: { roles: ['Admin'] } },
     {path:"update-course", component: UpdateCourseComponent, canActivate: [authGuard], data: { roles: ['Admin'] }},
     {path:"dancers", component: DancersComponent,  canActivate: [authGuard], data: { 
          roles: ['Admin','HipHop_Coach','Latin_Coach','Zumba_Coach'] } },
     {path:"add-dancer", component: AddDancerComponent, canActivate: [authGuard], data: { roles: ['Admin'] } },
     {path:"update-dancer", component: UpdateDancerComponent,  canActivate: [authGuard], data: { 
          roles: ['Admin','HipHop_Coach','Latin_Coach','Zumba_Coach'] } },
     {path:"course-schedule", component: CourseScheduleComponent, canActivate: [authGuard], data: { 
          roles: ['Admin','HipHop_Coach','Latin_Coach','Zumba_Coach']} },
];
