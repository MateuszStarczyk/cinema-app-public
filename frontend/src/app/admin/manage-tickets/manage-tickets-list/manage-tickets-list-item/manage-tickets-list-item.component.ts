import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TicketKind} from '../../../../shared/models/ticket-kind';
import {AlertService} from '../../../../shared/services/alerts/alert.service';
import {ManageTicketsService} from '../../../shared/services/manage-tickets.service';

@Component({
  selector: 'app-manage-tickets-list-item',
  templateUrl: './manage-tickets-list-item.component.html',
  styleUrls: ['./manage-tickets-list-item.component.scss']
})
export class ManageTicketsListItemComponent {
  isLoading = false;
  @Input() ticketKind: TicketKind;
  @Output() deleted: EventEmitter<TicketKind> = new EventEmitter();
  @Output() saved: EventEmitter<TicketKind> = new EventEmitter();

  constructor(
    private manageTicketsService: ManageTicketsService,
    private alerts: AlertService
  ) { }

  saveTicket() {
    this.isLoading = true;
    if (this.ticketKind.id) {
      this.manageTicketsService.editManagedTicket(this.ticketKind).subscribe(
        () => this.saved.emit()
      ).add(() => this.isLoading = false);
    } else {
      this.manageTicketsService.addManagedTicket(this.ticketKind).subscribe(
        () => this.saved.emit()
      ).add(() => this.isLoading = false);
    }
  }

  askToConfirmDeleteTicket() {
    this.alerts
      .showYesNoDialog(`Do you want to delete managed ticket ${this.ticketKind.name}?`)
      .then((result) => {
        if (result.value) {
          this.deleteTicket();
        }
      });
  }

  deleteTicket() {
    this.isLoading = true;
    this.manageTicketsService.deleteManagedTicket(this.ticketKind.id).subscribe(
      () => this.deleted.emit()
    ).add(() => this.isLoading = false);
  }
}
