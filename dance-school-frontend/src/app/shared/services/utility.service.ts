import { Injectable, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Injectable({
     providedIn: 'root'
})
export class UtilityService {
     dialog = inject(MatDialog)

     constructor() { }

     openInfoDialog(message: string): void {
          this.dialog.open(ConfirmDialogComponent, {
          data: {
               message: message,
               messageType: 'info'
          },
          panelClass: 'custom-dialog-container'
          });
     }
}
