import { Component } from '@angular/core';
import { Movie } from 'src/app/shared/models/movie';
import { ManageMoviesService } from 'src/app/admin/shared/services/manage-movies.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-manage-movies-add-movie',
  templateUrl: './manage-movies-add-movie.component.html',
  styleUrls: ['./manage-movies-add-movie.component.scss'],
})
export class ManageMoviesAddMovieComponent {
  isLoading = false;

  constructor(
    private readonly manageMoviesService: ManageMoviesService,
    private readonly router: Router
  ) {}

  addMovie(newMovie: Movie) {
    this.isLoading = true;
    this.manageMoviesService.addMovie(newMovie).subscribe(
      addedMovie => this.router.navigate(['admin', 'movies', 'edit', addedMovie.id])
    ).add(() => this.isLoading = false);
  }
}
