import { Component, OnInit } from '@angular/core';
import { TicketKind } from '../../../shared/models/ticket-kind';
import {ManageTicketsService} from '../../shared/services/manage-tickets.service';

@Component({
  selector: 'app-manage-tickets-list',
  templateUrl: './manage-tickets-list.component.html',
  styleUrls: ['./manage-tickets-list.component.scss']
})
export class ManageTicketsListComponent implements OnInit {
  isLoading = false;
  tickets: TicketKind[];

  constructor(private readonly manageTicketsService: ManageTicketsService) {}

  ngOnInit() {
    this.loadTickets();
  }

  newTicketKind() {
    this.tickets.push(new TicketKind());
  }

  loadTickets() {
    this.isLoading = true;
    this.tickets = [];
    this.manageTicketsService.getAllManagedTickets().subscribe(
      (tickets) => this.tickets = tickets
    ).add(() => this.isLoading = false);
  }

}
