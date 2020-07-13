import { Component, OnInit } from '@angular/core';
import { ReservedTicketKind } from '../shared/models/reserved-ticket-kind';
import { Screening } from '../shared/models/screening';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ChooseSeatsComponent } from './choose-seats/choose-seats.component';
import { ReservationService } from '../shared/services/reservation.service';
import { Reservation } from '../shared/models/reservation';
import { AuthenticationService } from 'src/app/authentication/shared/services/authentication.service';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.scss'],
})
export class ReservationComponent implements OnInit {
  screening: Screening;
  selectedSeats: string[] = [];
  reservedTicketKinds: ReservedTicketKind[] = [];
  seatsQty = 0;
  totalPrice = 0;
  availableSeatsQty = 0;
  isSoldOut = false;
  isOutdated = false;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly dialog: MatDialog,
    private readonly router: Router,
    private readonly reservationService: ReservationService,
    private readonly authenticationService: AuthenticationService
  ) {}

  ngOnInit() {
    this.screening = this.route.snapshot.data.screening;
    this.availableSeatsQty = this.screening.freeSeatsQuantity;
    if (this.screening.freeSeatsQuantity === 0) {
      this.isSoldOut = true;
    }
    if (new Date(this.screening.startDate) < new Date()) {
      this.isOutdated = true;
    }
  }

  reservedTicketKindChanged(reservedTicketKinds: ReservedTicketKind[]) {
    this.reservedTicketKinds = reservedTicketKinds;
    this.selectedSeats = [];
    this.seatsQty = this.reservedTicketKinds
      .map((tk) => tk.qty)
      .reduce((acc, qty) => acc + qty, 0);
    this.totalPrice = this.reservedTicketKinds
      .map((tk) => tk.qty * tk.price)
      .reduce((acc, pr) => acc + pr, 0);
  }

  openChooseSeatsModal() {
    const dialogRef = this.dialog.open(ChooseSeatsComponent, {
      height: '90vh',
      width: '90vw',
      data: {
        seatsQty: this.seatsQty,
        selectedSeats: this.selectedSeats,
        screeningId: this.screening.id,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.selectedSeats = result;
    });
  }

  buyTicket() {
    const newReservation = this.generateReservation();
    this.reservationService
      .addReservation(newReservation)
      .subscribe((reservation: Reservation) => {
        this.router.navigate(['ticket', reservation.number]);
      });
  }

  generateReservation(): Reservation {
    const newReservation = new Reservation();
    newReservation.screeningId = this.screening.id;
    newReservation.ticketKinds = this.reservedTicketKinds.filter(tk => tk.qty > 0);
    newReservation.selectedSeats = this.selectedSeats;
    newReservation.customerId = this.authenticationService.getLoggedInUser().id;
    return newReservation;
  }
}
