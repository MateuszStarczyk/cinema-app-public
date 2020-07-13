import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ScreeningsComponent } from './screenings/screenings.component';
import { ScreeningsListComponent } from './screenings/screenings-list/screenings-list.component';
import { ScreeningsListItemComponent } from './screenings/screenings-list/screenings-list-item/screenings-list-item.component';
import { SharedModule } from '../shared/shared.module';
import { ReservationComponent } from './reservation/reservation.component';
import { ChooseTicketKindComponent } from './reservation/choose-ticket-kind/choose-ticket-kind.component';
import { ChooseTicketKindItemComponent } from './reservation/choose-ticket-kind/choose-ticket-kind-item/choose-ticket-kind-item.component';
import { ChooseSeatsComponent } from './reservation/choose-seats/choose-seats.component';
import { SeatsPlanComponent } from './reservation/choose-seats/seats-plan/seats-plan.component';
import { TicketComponent } from './ticket/ticket.component';
import { TicketKindsComponent } from './ticket/ticket-kinds/ticket-kinds.component';
import {PendingTicketsComponent} from './my-tickets/pending-tickets/pending-tickets.component';
import {PendingTicketsListComponent} from './my-tickets/pending-tickets/pending-tickets-list/pending-tickets-list.component';
import {MyTicketsItemComponent} from './my-tickets/my-tickets-item/my-tickets-item.component';
import {TicketsHistoryComponent} from './my-tickets/tickets-history/tickets-history.component';
import {TicketsHistoryListComponent} from './my-tickets/tickets-history/tickets-history-list/tickets-history-list.component';

@NgModule({
  imports: [CommonModule, SharedModule],
  declarations: [
    ScreeningsComponent,
    ScreeningsListComponent,
    ScreeningsListItemComponent,
    ReservationComponent,
    ChooseTicketKindComponent,
    ChooseTicketKindItemComponent,
    ChooseSeatsComponent,
    SeatsPlanComponent,
    TicketComponent,
    TicketKindsComponent,
    PendingTicketsComponent,
    PendingTicketsListComponent,
    MyTicketsItemComponent,
    TicketsHistoryComponent,
    TicketsHistoryListComponent
  ],
  exports: [
    ScreeningsListComponent,
    ScreeningsListItemComponent
  ],
  entryComponents: [
    ChooseSeatsComponent
  ]
})
export class ReservationModule {}
