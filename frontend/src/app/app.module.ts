import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { AuthenticationModule } from './authentication/authentication.module';
import { SharedModule } from './shared/shared.module';
import { HomeComponent } from './home/home.component';
import { AdminModule } from './admin/admin.module';
import { ReservationModule } from './reservation/reservation.module';
import { TodayScreeningsComponent } from './home/today-screenings/today-screenings.component';
import { TicketOptionsComponent } from './ticket-options/ticket-options.component';
import { WhatsOnComponent } from './whats-on/whats-on.component';
import { WhatsOnMovieItemComponent } from './whats-on/whats-on-movie-item/whats-on-movie-item.component';
import { MovieDetailsComponent } from './whats-on/movie-details/movie-details.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    TodayScreeningsComponent,
    TicketOptionsComponent,
    WhatsOnComponent,
    WhatsOnMovieItemComponent,
    MovieDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SharedModule,
    AuthenticationModule,
    AdminModule,
    ReservationModule
  ],
  entryComponents: [
    MovieDetailsComponent
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
