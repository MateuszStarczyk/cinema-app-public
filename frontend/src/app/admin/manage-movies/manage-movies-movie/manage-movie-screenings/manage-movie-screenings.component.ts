import { Component, OnInit, Input } from '@angular/core';
import { ManageMoviesService } from 'src/app/admin/shared/services/manage-movies.service';
import { Screening } from 'src/app/admin/shared/models/screening';

@Component({
  selector: 'app-manage-movie-screenings',
  templateUrl: './manage-movie-screenings.component.html',
  styleUrls: ['./manage-movie-screenings.component.scss'],
})
export class ManageMovieScreeningsComponent implements OnInit {
  isLoading = false;
  screenings: Screening[];
  @Input() movieId: string;

  constructor(
    private readonly manageMoviesService: ManageMoviesService
  ) {}

  ngOnInit() {
    this.loadScreenings();
  }

  loadScreenings() {
    this.isLoading = true;
    this.screenings = [];
    this.manageMoviesService.getAllScreeningsForMovie(this.movieId).subscribe(
      (screenings) => this.screenings = screenings
    ).add(() => this.isLoading = false);
  }
}
