import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { TicketKind } from '../shared/models/ticket-kind';
import { GeneralInformationService } from '../shared/services/general-information/general-information.service';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class TicketOptionsResolver implements Resolve<TicketKind[] | null> {
  constructor(private readonly informationService: GeneralInformationService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<TicketKind[] | null> | TicketKind[] | null {
    return this.informationService.getAllTicketKinds()
    .pipe(
      catchError(_ => [])
    );
  }
}
