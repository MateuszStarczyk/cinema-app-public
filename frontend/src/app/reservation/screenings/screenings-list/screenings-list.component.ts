import { Component, Input } from '@angular/core';
import { MovieWithScreenings } from '../../shared/models/movie-with-screenings';

@Component({
  selector: 'app-screenings-list',
  templateUrl: './screenings-list.component.html',
  styleUrls: ['./screenings-list.component.scss']
})
export class ScreeningsListComponent {

  @Input() screenings: MovieWithScreenings[];
  @Input() isLoading: boolean;

  constructor() { }
}
