import { Component, Input } from '@angular/core';
import { ReservedTicketKind } from '../../shared/models/reserved-ticket-kind';

@Component({
  selector: 'app-ticket-kinds',
  templateUrl: './ticket-kinds.component.html',
  styleUrls: ['./ticket-kinds.component.scss'],
})
export class TicketKindsComponent {
  ticketKindList: ReservedTicketKind[] = [];
  displayedColumns: string[] = ['type', 'price', 'qty'];
  qty = 0;
  totalPrice = 0;

  @Input()
  set ticketKinds(ticketKindsValue: ReservedTicketKind[]) {
    this.ticketKindList = ticketKindsValue;
    this.qty = this.ticketKindList
      .map((tk) => tk.qty)
      .reduce((acc, qty) => acc + qty, 0);
    this.totalPrice = this.ticketKindList
      .map((tk) => tk.qty * tk.price)
      .reduce((acc, pr) => acc + pr, 0);
  }

  constructor() {}
}
