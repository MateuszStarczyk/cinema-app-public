import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { TicketKind } from 'src/app/shared/models/ticket-kind';
import { ReservedTicketKind } from '../../shared/models/reserved-ticket-kind';
import { GeneralInformationService } from 'src/app/shared/services/general-information/general-information.service';

@Component({
  selector: 'app-choose-ticket-kind',
  templateUrl: './choose-ticket-kind.component.html',
  styleUrls: ['./choose-ticket-kind.component.scss'],
})
export class ChooseTicketKindComponent implements OnInit {

  isLoading = false;
  reservedTicketKinds: ReservedTicketKind[] = [];
  @Input() availableSeatsQty = 0;
  @Output() ticketKindsUpdated: EventEmitter<any> = new EventEmitter();

  constructor(private readonly ticketService: GeneralInformationService) {
    this.loadTicketKinds();
  }

  ngOnInit() {}

  loadTicketKinds() {
    this.isLoading = true;
    this.reservedTicketKinds = [];
    this.ticketService
      .getAllTicketKinds()
      .subscribe((ticketKinds: TicketKind[]) => {
        this.reservedTicketKinds = ticketKinds.map(
          (tk) => new ReservedTicketKind(tk.id, tk.name, tk.price)
        );
      })
      .add(() => (this.isLoading = false));
  }

  qtyChanged() {
    this.ticketKindsUpdated.emit(this.reservedTicketKinds);
  }
}
