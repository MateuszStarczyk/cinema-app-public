import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './authentication/login/login.component';
import { NotFoundComponent } from './shared/not-found/not-found.component';
import { HomeComponent } from './home/home.component';
import { SignUpComponent } from './authentication/sign-up/sign-up.component';
import { LogoutComponent } from './authentication/logout/logout.component';
import { ManageMoviesComponent } from './admin/manage-movies/manage-movies.component';
import { ManageTicketsComponent } from './admin/manage-tickets/manage-tickets.component';
import { ManageMoviesEditMovieComponent } from './admin/manage-movies/manage-movies-movie/manage-movies-edit-movie/manage-movies-edit-movie.component';
import { ManageMoviesEditMovieResolver } from './admin/manage-movies/manage-movies-movie/manage-movies-edit-movie/manage-movies-edit-movie.resolver';
import { ManageMoviesAddMovieComponent } from './admin/manage-movies/manage-movies-movie/manage-movies-add-movie/manage-movies-add-movie.component';
import { AdminAuthGuard } from './shared/helpers/admin-auth.guard';
import { ScreeningsComponent } from './reservation/screenings/screenings.component';
import { ReservationComponent } from './reservation/reservation/reservation.component';
import { ReservationResolver } from './reservation/reservation/reservation.resolver';
import { TicketComponent } from './reservation/ticket/ticket.component';
import { AuthGuard } from './shared/helpers/auth.guard';
import { TicketResolver } from './reservation/ticket/ticket.resolver';
import { PendingTicketsComponent } from './reservation/my-tickets/pending-tickets/pending-tickets.component';
import { TicketsHistoryComponent } from './reservation/my-tickets/tickets-history/tickets-history.component';
import { PendingTicketsListResolver } from './reservation/my-tickets/pending-tickets/pending-tickets-list/pending-tickets-list.resolver';
import { TicketsHistoryListResolver } from './reservation/my-tickets/tickets-history/tickets-history-list/tickets-history-list.resolver';
import { TicketOptionsComponent } from './ticket-options/ticket-options.component';
import { WhatsOnComponent } from './whats-on/whats-on.component';
import { TicketOptionsResolver } from './ticket-options/ticket-options.resolver';
import { WhatsOnResolver } from './whats-on/whats-on.resolver';
import { TodayScreeningsResolver } from './home/today-screenings/today-screenings.resolver';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
  },
  {
    path: 'home',
    component: HomeComponent,
    resolve : {
      movies: WhatsOnResolver,
      screenings: TodayScreeningsResolver
    }
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'logout',
    component: LogoutComponent,
  },
  {
    path: 'sign-up',
    component: SignUpComponent,
  },
  {
    path: 'tickets',
    component: TicketOptionsComponent,
    resolve : {
      tickets: TicketOptionsResolver
    }
  },
  {
    path: 'whats-on',
    component: WhatsOnComponent,
    resolve : {
      movies: WhatsOnResolver
    }
  },
  {
    path: 'my-tickets',
    component: PendingTicketsComponent,
    canActivate: [AuthGuard],
    resolve : {
      tickets: PendingTicketsListResolver
    }
  },
  {
    path: 'tickets-history',
    component: TicketsHistoryComponent,
    canActivate: [AuthGuard],
    resolve : {
      tickets: TicketsHistoryListResolver
    }
  },
  {
    path: 'ticket/:reservationId',
    component: TicketComponent,
    resolve : {
      ticket: TicketResolver
    },
    canActivate: [AuthGuard]
  },
  {
    path: 'screenings',
    children: [
      {
        path: '',
        component: ScreeningsComponent,
      },
      {
        path: ':screeningId/reservation',
        component: ReservationComponent,
        resolve : {
          screening: ReservationResolver
        },
        canActivate: [AuthGuard]
      }
    ]
  },
  {
    path: 'admin',
    children: [
      {
        path: 'movies',
        children: [
          {
            path: '',
            component: ManageMoviesComponent,
          },
          {
            path: 'edit/:movieId',
            component: ManageMoviesEditMovieComponent,
            resolve: {
              movie: ManageMoviesEditMovieResolver,
            },
          },
          {
            path: 'add',
            component: ManageMoviesAddMovieComponent,
          },
        ],
      },
      {
        path: 'tickets',
        component: ManageTicketsComponent,
      },
    ],
    canActivate: [AdminAuthGuard],
  },
  {
    path: '**',
    component: NotFoundComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
