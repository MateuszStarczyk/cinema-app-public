import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Ticket } from '../shared/models/ticket';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.scss'],
})
export class TicketComponent implements OnInit {
  ticket: Ticket;

  constructor(private readonly route: ActivatedRoute) {}

  ngOnInit() {
    this.ticket = this.route.snapshot.data.ticket;
  }
}
