import { Component, Input, EventEmitter, Output } from '@angular/core';
import { Movie } from 'src/app/shared/models/movie';
import { ManageMoviesService } from 'src/app/admin/shared/services/manage-movies.service';
import { AlertService } from 'src/app/shared/services/alerts/alert.service';

@Component({
  selector: 'app-manage-movies-list-item',
  templateUrl: './manage-movies-list-item.component.html',
  styleUrls: ['./manage-movies-list-item.component.scss'],
})
export class ManageMoviesListItemComponent {
  isLoading = false;
  @Input() movie: Movie;
  @Output() deleted: EventEmitter<any> = new EventEmitter();

  constructor(
    private readonly manageMoviesService: ManageMoviesService,
    private readonly alerts: AlertService
  ) {}

  askToConfirmDeleteMovie() {
    this.alerts
      .showYesNoDialog(`Do you want to delete movie ${this.movie.title}?`)
      .then((result) => {
        if (result.value) {
          this.deleteMovie();
        }
      });
  }

  deleteMovie() {
    this.isLoading = true;
    this.manageMoviesService.deleteMovie(this.movie.id).subscribe(
      () => this.deleted.emit()
    ).add(() => this.isLoading = false);
  }
}
