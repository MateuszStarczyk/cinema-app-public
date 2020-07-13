import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Movie } from 'src/app/shared/models/movie';
import { AppSettings } from 'src/app/shared/settings/app-settings';

@Component({
  selector: 'app-manage-movie-form',
  templateUrl: './manage-movie-form.component.html',
  styleUrls: ['./manage-movie-form.component.scss'],
})
export class ManageMovieFormComponent {
  @Input() movie: Movie = new Movie();
  @Input() isEditMode = false;
  @Output() save: EventEmitter<Movie> = new EventEmitter();
  placeholder = AppSettings.POSTER_PLACEHOLDER;

  constructor() {}

  onSave() {
    this.save.emit(this.movie);
  }
}
