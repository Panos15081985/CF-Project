import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [MatDialogModule, FlexLayoutModule,CommonModule],
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { message: string, messageType: 'info' | 'confirmation' }
  ) {}

  onConfirm(): void {
    this.dialogRef.close(true); // Επιστρέφει "true" αν επιβεβαιωθεί η ενέργεια
  }

  onCancel(): void {
    this.dialogRef.close(false); // Επιστρέφει "false" αν ακυρωθεί η ενέργεια
  }
}
