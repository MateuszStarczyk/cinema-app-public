import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Movie } from '../../../shared/models/movie';
import { Observable, throwError } from 'rxjs';
import { Screening } from '../models/screening';
import { environment } from 'src/environments/environment';
import { catchError, map } from 'rxjs/operators';
import { AlertService } from 'src/app/shared/services/alerts/alert.service';
import { Room } from '../models/room';

@Injectable({
  providedIn: 'root',
})
export class ManageMoviesService {
  baseUrl = environment.baseUrl + '/api/cinema-management';

  constructor(
    private readonly http: HttpClient,
    private readonly alerts: AlertService
  ) {}

  getAllMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.baseUrl}/movies`).pipe(
      catchError((err) => {
        this.alerts.showCustomErrorMessage(err.error.message);
        return throwError(err);
      })
    );
  }

  getMovieById(movieId: string): Observable<Movie> {
    return this.http
      .get<Movie>(`${this.baseUrl}/movies/${movieId}`)
      .pipe(
        catchError((err) => {
          this.alerts.showCustomErrorMessage(err.error.message);
          return throwError(err);
        })
      );
  }

  addMovie(movie: Movie): Observable<Movie> {
    return this.http.post<any>(`${this.baseUrl}/movies`, movie).pipe(
      map(addedMovie => {
        this.alerts.showCustomSuccessMessage('Added movie');
        return addedMovie;
      }),
      catchError((err) => {
        this.alerts.showCustomErrorMessage(err.error.message);
        return throwError(err);
      })
    );
  }

  editMovie(movie: Movie): Observable<any> {
    return this.http
      .put<any>(`${this.baseUrl}/movies/${movie.id}`, movie)
      .pipe(
        map(() => {
          this.alerts.showCustomSuccessMessage('Edited movie');
        }),
        catchError((err) => {
          this.alerts.showCustomErrorMessage(err.error.message);
          return throwError(err);
        })
      );
  }

  deleteMovie(movieId: string) {
    return this.http
      .delete<any>(`${this.baseUrl}/movies/${movieId}`)
      .pipe(
        map(() => {
          this.alerts.showCustomSuccessMessage('Deleted movie');
        }),
        catchError((err) => {
          this.alerts.showCustomErrorMessage(err.error);
          return throwError(err);
        })
      );
  }

  getAllScreeningsForMovie(movieId: string): Observable<Screening[]> {
    return this.http.get<Screening[]>(`${this.baseUrl}/movies/${movieId}/screenings`);
  }

  addScreening(screening: Screening): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/screenings`, screening).pipe(
      map(() => {
        this.alerts.showCustomSuccessMessage('Added screening');
      }),
      catchError((err) => {
        this.alerts.showCustomErrorMessage(err.error.message);
        return throwError(err);
      })
    );
  }

  deleteScreening(screeningId: number) {
    return this.http.delete<any>(`${this.baseUrl}/screenings/${screeningId}`).pipe(
      map(() => {
        this.alerts.showCustomSuccessMessage('Deleted screening');
      }),
      catchError((err) => {
        this.alerts.showCustomErrorMessage(err.error);
        return throwError(err);
      })
    );
  }

  getRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(`${this.baseUrl}/rooms`);
  }
}
