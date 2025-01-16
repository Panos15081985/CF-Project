import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { CoachService } from '../services/coach.service';
import { UtilityService } from '../services/utility.service';

export const authGuard: CanActivateFn = (route, state) => {
  
     const coachService = inject(CoachService);
     const utilityService = inject(UtilityService);

     const requiredRoles = route.data["roles"] as string[] || [];

     if(coachService.user() && coachService.hasAnyRole(requiredRoles)){
          return true;
     }

     utilityService.openInfoDialog(
          "Δεν έχετε τα απαραίτητα δικαιώματα για να αποκτήσετε πρόσβαση σε αυτή τη σελίδα."
     )

     return false
 };
