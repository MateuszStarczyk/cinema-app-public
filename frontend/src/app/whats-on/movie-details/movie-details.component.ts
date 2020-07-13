import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Movie } from 'src/app/shared/models/movie';
import { AppSettings } from 'src/app/shared/settings/app-settings';

@Component({
  selector: 'app-movie-details',
  templateUrl: './movie-details.component.html',
  styleUrls: ['./movie-details.component.scss'],
})
export class MovieDetailsComponent implements OnInit {
  movie: Movie;
  placeholder = AppSettings.POSTER_PLACEHOLDER;

  constructor(@Inject(MAT_DIALOG_DATA) private readonly data: any) {
    this.movie = data.movie;
  }

  ngOnInit() {
    const tag = document.createElement('script');
    tag.src = AppSettings.TRAILER_API_URL;
    document.body.appendChild(tag);
  }
}
