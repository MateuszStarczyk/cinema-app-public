import { Component, OnInit } from '@angular/core';
import { Movie } from '../../../shared/models/movie';
import { ManageMoviesService } from '../../shared/services/manage-movies.service';

@Component({
  selector: 'app-manage-movies-list',
  templateUrl: './manage-movies-list.component.html',
  styleUrls: ['./manage-movies-list.component.scss'],
})
export class ManageMoviesListComponent implements OnInit {
  isLoading = false;
  movies: Movie[];

  constructor(private readonly manageMoviesService: ManageMoviesService) {}

  ngOnInit() {
    this.loadMovies();
  }

  loadMovies() {
    this.isLoading = true;
    this.movies = [];
    this.manageMoviesService.getAllMovies().subscribe(
      (movies) => this.movies = movies
    ).add(() => this.isLoading = false);
  }
}
