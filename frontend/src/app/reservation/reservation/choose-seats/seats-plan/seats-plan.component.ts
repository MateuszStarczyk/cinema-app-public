import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ScreeningSeat } from 'src/app/reservation/shared/models/screening-seat';
import { SeatsRow } from 'src/app/reservation/shared/models/seats-row';

@Component({
  selector: 'app-seats-plan',
  templateUrl: './seats-plan.component.html',
  styleUrls: ['./seats-plan.component.scss'],
})
export class SeatsPlanComponent {
  @Input() selectedSeats: string[] = [];
  allSeats: ScreeningSeat[] = [];
  rows: SeatsRow[] = [];
  @Output() seatSelected: EventEmitter<string> = new EventEmitter();

  @Input()
  set seats(seatsValue: ScreeningSeat[]) {
    this.allSeats = seatsValue;
    this.divideSeatsIntoRows();
  }

  constructor() {}

  seatSelection(seatNumber: string) {
    this.seatSelected.emit(seatNumber);
  }

  divideSeatsIntoRows() {
    this.allSeats.forEach((seat) => {
      const seatRowId = seat.seatNumber.split('-')[0];
      const existingRow = this.rows.find((r) => r.id === seatRowId);
      if (existingRow) {
        existingRow.seats.push(seat);
      } else {
        const newRow = new SeatsRow(seatRowId);
        newRow.seats.push(seat);
        this.rows.push(newRow);
      }
    });
  }
}
