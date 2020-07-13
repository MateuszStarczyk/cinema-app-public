import { ReservedTicketKind } from './reserved-ticket-kind';

export class Reservation {
    number: string;
    screeningId: number;
    customerId: number;
    ticketKinds: ReservedTicketKind[];
    selectedSeats: string[];
}
