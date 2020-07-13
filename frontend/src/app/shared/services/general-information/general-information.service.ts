import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { TicketKind } from 'src/app/shared/models/ticket-kind';
import { Observable } from 'rxjs';
import { Movie } from 'src/app/shared/models/movie';

@Injectable({
  providedIn: 'root'
})
export class GeneralInformationService {

  baseUrlManagement = environment.baseUrl + '/api/cinema-management';
  baseApiUrl = environment.baseUrl + '/api';

  constructor(
    private readonly http: HttpClient
  ) {}

  getAllTicketKinds(): Observable<TicketKind[]> {
    return this.http.get<TicketKind[]>(`${this.baseUrlManagement}/managed-tickets`);
  }

  getAllCurrentMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.baseApiUrl}/whats-on`);
  }

}
