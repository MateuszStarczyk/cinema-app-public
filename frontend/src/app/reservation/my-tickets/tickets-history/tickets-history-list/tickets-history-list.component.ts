import { Component, OnInit, ViewChild } from '@angular/core';
import { Ticket } from '../../../shared/models/ticket';
import { ActivatedRoute } from '@angular/router';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-tickets-history-list',
  templateUrl: './tickets-history-list.component.html',
  styleUrls: ['./tickets-history-list.component.scss'],
})
export class TicketsHistoryListComponent implements OnInit {
  columns: string[] = ['ticket'];
  tickets: MatTableDataSource<Ticket>;
  dateFilter: Date = null;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly datepipe: DatePipe
  ) {}

  ngOnInit() {
    this.tickets = new MatTableDataSource(this.route.snapshot.data.tickets);
    this.tickets.paginator = this.paginator;
  }

  applyFilter(date: Date) {
    if (date) {
      this.tickets.filterPredicate = (ticket, filter) =>
        this.datepipe.transform(ticket.startDate, 'yyyy-MM-dd') === filter;
      this.tickets.filter = this.datepipe.transform(date, 'yyyy-MM-dd');
    } else {
      this.tickets.filter = null;
    }

    if (this.tickets.paginator) {
      this.tickets.paginator.firstPage();
    }
  }

  clearFilter() {
    this.dateFilter = undefined;
    this.applyFilter(null);
  }
}
