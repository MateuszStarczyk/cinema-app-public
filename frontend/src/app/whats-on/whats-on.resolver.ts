import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { GeneralInformationService } from '../shared/services/general-information/general-information.service';
import { Movie } from '../shared/models/movie';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class WhatsOnResolver implements Resolve<Movie[] | null> {
  constructor(private readonly informationService: GeneralInformationService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<Movie[] | null> | Movie[] | null {
    return this.informationService
      .getAllCurrentMovies()
      .pipe(catchError((_) => []));
  }
}
