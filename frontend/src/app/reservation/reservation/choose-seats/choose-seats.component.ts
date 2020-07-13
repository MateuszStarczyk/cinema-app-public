import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ScreeningSeat } from '../../shared/models/screening-seat';
import { ReservationService } from '../../shared/services/reservation.service';
import { AlertService } from 'src/app/shared/services/alerts/alert.service';

@Component({
  selector: 'app-choose-seats',
  templateUrl: './choose-seats.component.html',
  styleUrls: ['./choose-seats.component.scss'],
})
export class ChooseSeatsComponent {
  isLoading = false;
  selectedSeats: string[] = [];
  allSeats: ScreeningSeat[] = [];
  seatsQty = 0;

  constructor(
    private readonly dialogRef: MatDialogRef<ChooseSeatsComponent>,
    @Inject(MAT_DIALOG_DATA) private readonly data: any,
    private readonly reservationService: ReservationService,
    private readonly alerts: AlertService
  ) {
    dialogRef.disableClose = true;
    this.selectedSeats = data.selectedSeats;
    this.seatsQty = data.seatsQty;
    this.loadSeatsData();
  }

  loadSeatsData() {
    this.isLoading = true;
    this.reservationService
      .getScreeningSeatAvailability(this.data.screeningId)
      .subscribe((screeningSeats: ScreeningSeat[]) => {
        this.allSeats = screeningSeats;
        this.checkForTakenSelectedSeats();
      }, err => {
        this.selectedSeats = [];
        this.alerts.showCustomErrorMessage(
          'There was an error loading seats. Try again!'
        );
      })
      .add(() => {
        this.isLoading = false;
      });
  }

  seatSelected(seatNumber: string) {
    if (this.selectedSeats.includes(seatNumber)) {
      this.selectedSeats = this.selectedSeats.filter((s) => s !== seatNumber);
    } else if (this.selectedSeats.length < this.seatsQty) {
      this.selectedSeats.push(seatNumber);
    }
  }

  save() {
    this.dialogRef.close(this.selectedSeats);
  }

  checkForTakenSelectedSeats() {
    if (
      this.selectedSeats.find(
        (s) => !this.allSeats.find((st) => st.seatNumber === s)?.isAvailable
      )
    ) {
      this.selectedSeats = [];
      this.alerts.showCustomWarningMessage(
        'Your selected seats have been taken! Choose new seats.'
      );
    }
  }
}
