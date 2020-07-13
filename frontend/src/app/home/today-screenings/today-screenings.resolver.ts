import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { MovieWithScreenings } from 'src/app/reservation/shared/models/movie-with-screenings';
import { ReservationService } from 'src/app/reservation/shared/services/reservation.service';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class TodayScreeningsResolver implements Resolve<MovieWithScreenings[] | null> {
  constructor(private readonly reservationService: ReservationService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<MovieWithScreenings[] | null> | MovieWithScreenings[] | null {
    return this.reservationService.getAllMovieScreeningsForDate(new Date())
    .pipe(
      catchError(_ => [])
    );
  }
}
