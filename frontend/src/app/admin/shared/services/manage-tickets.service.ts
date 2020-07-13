import { Injectable } from '@angular/core';
import {environment} from '../../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {AlertService} from '../../../shared/services/alerts/alert.service';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {TicketKind} from '../../../shared/models/ticket-kind';

@Injectable({
  providedIn: 'root'
})
export class ManageTicketsService {
  baseUrl = environment.baseUrl + '/api/cinema-management';

  constructor(
    private readonly http: HttpClient,
    private readonly alerts: AlertService
  ) {}

  getAllManagedTickets(): Observable<TicketKind[]> {
    return this.http.get<TicketKind[]>(`${this.baseUrl}/managed-tickets`)
    .pipe(
      catchError((err) => {
        this.alerts.showCustomErrorMessage(err.error.message);
        return throwError(err);
      })
    );
  }

  addManagedTicket(ticketKind: TicketKind): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/managed-tickets`, ticketKind)
    .pipe(
      catchError((err) => {
        this.alerts.showCustomErrorMessage(err.error.message);
        return throwError(err);
      })
    );
  }

  editManagedTicket(ticketKind: TicketKind): Observable<any> {
    return this.http
      .put<any>(`${this.baseUrl}/managed-tickets/${ticketKind.id}`, ticketKind)
      .pipe(
        catchError((err) => {
          this.alerts.showCustomErrorMessage(err.error.message);
          return throwError(err);
        })
      );
  }

  deleteManagedTicket(ticketId: number) {
    return this.http
      .delete<any>(`${this.baseUrl}/managed-tickets/${ticketId}`)
      .pipe(
        catchError((err) => {
          this.alerts.showCustomErrorMessage(err.error.message);
          return throwError(err);
        })
      );
  }
}
