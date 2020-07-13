import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ReservationService } from '../shared/services/reservation.service';
import { Ticket } from '../shared/models/ticket';

@Injectable({
  providedIn: 'root',
})
export class TicketResolver implements Resolve<Ticket | null> {
  constructor(private readonly reservationService: ReservationService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<Ticket | null> | Ticket | null {
    return this.reservationService.getTicketById(
      route.paramMap.get('reservationId')
    );
  }
}
