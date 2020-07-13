import {Component, Input} from '@angular/core';
import {Ticket} from '../../shared/models/ticket';

@Component({
  selector: 'app-my-tickets-item',
  templateUrl: './my-tickets-item.component.html',
  styleUrls: ['./my-tickets-item.component.scss']
})
export class MyTicketsItemComponent {
  @Input() ticket: Ticket;

  constructor(
  ) { }
}
