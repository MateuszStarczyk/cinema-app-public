import { Component } from '@angular/core';
import { MovieWithScreenings } from '../shared/models/movie-with-screenings';
import { ReservationService } from '../shared/services/reservation.service';

@Component({
  selector: 'app-screenings',
  templateUrl: './screenings.component.html',
  styleUrls: ['./screenings.component.scss'],
})
export class ScreeningsComponent {
  isLoading = false;
  screenings: MovieWithScreenings[];
  date = new Date();
  minDate = new Date();

  constructor(private readonly reservationService: ReservationService) {
    this.loadScreenings();
  }

  dateChanged(event: any) {
    this.date = event.value;
    this.loadScreenings();
  }

  loadScreenings() {
    this.isLoading = true;
    this.screenings = [];
    this.reservationService
      .getAllMovieScreeningsForDate(this.date)
      .subscribe((screenings) => {
        this.screenings = screenings;
      })
      .add(() => (this.isLoading = false));
  }
}
