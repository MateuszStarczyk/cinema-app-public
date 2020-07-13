import { Component, OnInit } from '@angular/core';
import { Movie } from 'src/app/shared/models/movie';
import { ActivatedRoute } from '@angular/router';
import { ManageMoviesService } from 'src/app/admin/shared/services/manage-movies.service';

@Component({
  selector: 'app-manage-movies-edit-movie',
  templateUrl: './manage-movies-edit-movie.component.html',
  styleUrls: ['./manage-movies-edit-movie.component.scss'],
})
export class ManageMoviesEditMovieComponent implements OnInit {
  isLoading = false;
  movie: Movie;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly manageMoviesService: ManageMoviesService
  ) {}

  ngOnInit() {
    this.movie = this.route.snapshot.data.movie;
  }

  saveChanges(editedMovie: Movie) {
    this.isLoading = true;
    this.manageMoviesService.editMovie(editedMovie).subscribe(
      () => this.movie = editedMovie
    ).add(() => this.isLoading = false);
  }
}
