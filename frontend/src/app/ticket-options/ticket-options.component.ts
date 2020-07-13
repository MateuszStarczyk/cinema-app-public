import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TicketKind } from '../shared/models/ticket-kind';

@Component({
  selector: 'app-ticket-options',
  templateUrl: './ticket-options.component.html',
  styleUrls: ['./ticket-options.component.scss'],
})
export class TicketOptionsComponent implements OnInit {
  ticketOptions: TicketKind[];
  columns: string[] = ['kind', 'price'];

  constructor(private readonly route: ActivatedRoute) {}

  ngOnInit() {
    this.ticketOptions = this.route.snapshot.data.tickets;
  }
}
