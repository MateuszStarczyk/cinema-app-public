import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Screening } from '../shared/models/screening';
import { ReservationService } from '../shared/services/reservation.service';

@Injectable({
  providedIn: 'root',
})
export class ReservationResolver implements Resolve<Screening | null> {
  constructor(private readonly reservationService: ReservationService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<Screening | null> | Screening | null {
    return this.reservationService.getScreeningById(
      route.paramMap.get('screeningId')
    );
  }
}
