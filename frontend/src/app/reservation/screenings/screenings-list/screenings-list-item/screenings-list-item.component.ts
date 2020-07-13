import { Component, Input } from '@angular/core';
import { MovieWithScreenings } from 'src/app/reservation/shared/models/movie-with-screenings';

@Component({
  selector: 'app-screenings-list-item',
  templateUrl: './screenings-list-item.component.html',
  styleUrls: ['./screenings-list-item.component.scss'],
})
export class ScreeningsListItemComponent {
  @Input() screening: MovieWithScreenings;

  constructor() {}

  hasDatePassed(date: Date) {
    return new Date(date) <= new Date();
  }
}
