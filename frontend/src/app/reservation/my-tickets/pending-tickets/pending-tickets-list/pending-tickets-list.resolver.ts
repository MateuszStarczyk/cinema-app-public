import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Ticket } from '../../../shared/models/ticket';
import { TicketService } from '../../../shared/services/ticket.service';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class PendingTicketsListResolver implements Resolve<Ticket[] | null> {
  constructor(private readonly ticketService: TicketService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<Ticket[] | null> | Ticket[] | null {
    return this.ticketService.getAllMyTickets()
    .pipe(
      catchError(_ => [])
    );
  }
}
