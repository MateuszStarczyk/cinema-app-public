import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManageTicketsComponent } from './manage-tickets/manage-tickets.component';
import { ManageMoviesComponent } from './manage-movies/manage-movies.component';
import { SharedModule } from '../shared/shared.module';
import { ManageMoviesListComponent } from './manage-movies/manage-movies-list/manage-movies-list.component';
import { ManageMoviesListItemComponent } from './manage-movies/manage-movies-list/manage-movies-list-item/manage-movies-list-item.component';
import { ManageTicketsListComponent } from './manage-tickets/manage-tickets-list/manage-tickets-list.component';
import { ManageTicketsListItemComponent } from './manage-tickets/manage-tickets-list/manage-tickets-list-item/manage-tickets-list-item.component';
import { ManageMovieFormComponent } from './manage-movies/manage-movies-movie/manage-movie-form/manage-movie-form.component';
import { ManageMovieScreeningsComponent } from './manage-movies/manage-movies-movie/manage-movie-screenings/manage-movie-screenings.component';
import { ManageMoviesAddMovieComponent } from './manage-movies/manage-movies-movie/manage-movies-add-movie/manage-movies-add-movie.component';
import { ManageMoviesEditMovieComponent } from './manage-movies/manage-movies-movie/manage-movies-edit-movie/manage-movies-edit-movie.component';
import { AddScreeningComponent } from './manage-movies/manage-movies-movie/manage-movie-screenings/add-screening/add-screening.component';
import { ScreeningItemComponent } from './manage-movies/manage-movies-movie/manage-movie-screenings/screening-item/screening-item.component';

@NgModule({
  imports: [CommonModule, SharedModule],
  declarations: [
    ManageTicketsComponent,
    ManageMoviesComponent,
    ManageMoviesListComponent,
    ManageMoviesListItemComponent,
    ManageTicketsComponent,
    ManageTicketsListComponent,
    ManageTicketsListItemComponent,
    ManageMovieFormComponent,
    ManageMovieScreeningsComponent,
    ManageMoviesAddMovieComponent,
    ManageMoviesEditMovieComponent,
    AddScreeningComponent,
    ScreeningItemComponent
  ],
})
export class AdminModule {}
