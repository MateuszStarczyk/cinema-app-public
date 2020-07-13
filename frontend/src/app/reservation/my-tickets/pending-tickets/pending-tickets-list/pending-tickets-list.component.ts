import { Component, OnInit } from '@angular/core';
import { Ticket } from '../../../shared/models/ticket';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-pending-tickets-list',
  templateUrl: './pending-tickets-list.component.html',
  styleUrls: ['./pending-tickets-list.component.scss']
})
export class PendingTicketsListComponent implements OnInit {

  tickets: Ticket[];

  constructor(private readonly route: ActivatedRoute) {}

  ngOnInit() {
    this.tickets = this.route.snapshot.data.tickets;
  }
}
