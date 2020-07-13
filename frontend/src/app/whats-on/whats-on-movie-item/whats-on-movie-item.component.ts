import { Component, OnInit, Input } from '@angular/core';
import { Movie } from 'src/app/shared/models/movie';
import { MatDialog } from '@angular/material/dialog';
import { MovieDetailsComponent } from '../movie-details/movie-details.component';
import { AppSettings } from 'src/app/shared/settings/app-settings';

@Component({
  selector: 'app-whats-on-movie-item',
  templateUrl: './whats-on-movie-item.component.html',
  styleUrls: ['./whats-on-movie-item.component.scss'],
})
export class WhatsOnMovieItemComponent implements OnInit {
  @Input() movie: Movie;
  @Input() isFull = true;
  descriptionClass = 'full-screen';
  placeholder = AppSettings.POSTER_PLACEHOLDER;

  constructor(private readonly dialog: MatDialog) {}

  ngOnInit() {
    if (!this.isFull) {
      this.descriptionClass = 'half-screen';
    }
  }

  openMovieDetailsModal() {
    this.dialog.open(MovieDetailsComponent, {
      height: '90vh',
      width: '90vw',
      data: {
        movie: this.movie,
      },
    });
  }
}
