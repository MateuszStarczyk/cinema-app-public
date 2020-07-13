import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import { AlertService } from 'src/app/shared/services/alerts/alert.service';
import { Observable, throwError } from 'rxjs';
import { MovieWithScreenings } from '../models/movie-with-screenings';
import { Screening } from '../models/screening';
import { ScreeningSeat } from '../models/screening-seat';
import { Reservation } from '../models/reservation';
import { catchError, map } from 'rxjs/operators';
import { Ticket } from '../models/ticket';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  baseUrl = environment.baseUrl + '/api/reservations';

  constructor(
    private readonly http: HttpClient,
    private readonly alerts: AlertService
  ) {}

  getAllMovieScreeningsForDate(date: Date): Observable<MovieWithScreenings[]> {
    const params = new HttpParams()
      .set('date', date.toDateString());
    return this.http.get<MovieWithScreenings[]>(`${this.baseUrl}/movies-with-screenings`, {params});
  }

  getScreeningById(screeningId: string): Observable<Screening> {
    return this.http.get<Screening>(`${this.baseUrl}/screenings/${screeningId}`);
  }

  getScreeningSeatAvailability(screeningId: string): Observable<ScreeningSeat[]> {
    return this.http.get<ScreeningSeat[]>(`${this.baseUrl}/screening-seats/${screeningId}`);
  }

  addReservation(reservation: Reservation): Observable<any> {
    this.alerts.showLoader();
    return this.http.post<any>(`${this.baseUrl}/reserve`, reservation).pipe(
      map((res) => {
        this.alerts.closeLoader();
        return res;
      }),
      catchError((err) => {
        this.alerts.closeLoader();
        this.alerts.showCustomErrorMessage(err.error);
        return throwError(err);
      })
    );
  }

  getTicketById(reservationId: string): Observable<Ticket> {
    return this.http.get<Ticket>(`${this.baseUrl}/${reservationId}`);
  }
}
