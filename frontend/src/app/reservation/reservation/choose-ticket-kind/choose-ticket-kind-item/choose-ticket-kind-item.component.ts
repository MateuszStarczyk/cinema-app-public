import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ReservedTicketKind } from 'src/app/reservation/shared/models/reserved-ticket-kind';

const DEFAULT_QTY = 10;

@Component({
  selector: 'app-choose-ticket-kind-item',
  templateUrl: './choose-ticket-kind-item.component.html',
  styleUrls: ['./choose-ticket-kind-item.component.scss']
})
export class ChooseTicketKindItemComponent {

  @Input() reservedTicketKind: ReservedTicketKind;
  quantities = [0];
  @Output() qtyChange: EventEmitter<any> = new EventEmitter();

  @Input()
  set availableQty(availableQty: number) {
    availableQty = availableQty + this.reservedTicketKind.qty;
    this.fillSeatsQtyList(availableQty < DEFAULT_QTY ? availableQty : DEFAULT_QTY);
  }

  constructor() { }

  qtyChanged() {
    this.qtyChange.emit();
  }

  fillSeatsQtyList(qty: number) {
    this.quantities = [0];
    for (let i = 1; i <= qty; i++) {
      this.quantities.push(i);
    }
  }

}
