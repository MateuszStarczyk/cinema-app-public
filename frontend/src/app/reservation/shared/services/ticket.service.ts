import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import {AlertService} from '../../../shared/services/alerts/alert.service';
import {Observable, throwError} from 'rxjs';
import {Ticket} from '../models/ticket';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  baseUrl = environment.baseUrl + '/api/tickets';

  constructor(
    private readonly http: HttpClient,
    private readonly alerts: AlertService
  ) {}

  getAllMyTickets(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${this.baseUrl}/my-tickets`).pipe(
      catchError((err) => {
        this.alerts.showCustomErrorMessage(err.error.message);
        return throwError(err);
      })
    );
  }

  getTicketsHistory(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${this.baseUrl}/history`).pipe(
      catchError((err) => {
        this.alerts.showCustomErrorMessage(err.error.message);
        return throwError(err);
      })
    );
  }
}
