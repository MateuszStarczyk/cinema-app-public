import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';
import { ManageMoviesService } from '../../../shared/services/manage-movies.service';
import { Injectable } from '@angular/core';
import { Movie } from '../../../../shared/models/movie';

@Injectable({
  providedIn: 'root',
})
export class ManageMoviesEditMovieResolver implements Resolve<Movie | null> {
  constructor(private readonly manageMoviesService: ManageMoviesService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<Movie | null> | Movie | null {
    return this.manageMoviesService.getMovieById(route.paramMap.get('movieId'));
  }
}
