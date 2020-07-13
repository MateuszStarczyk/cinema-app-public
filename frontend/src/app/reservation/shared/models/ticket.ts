import { ReservedTicketKind } from './reserved-ticket-kind';

export class Ticket {
    number: string;
    screeningId: number;
    customerId: number;
    customerEmail: string;
    customerName: string;
    ticketKinds: ReservedTicketKind[];
    selectedSeats: string[];
    startDate: Date;
    roomId: number;
    movieId: string;
    movieTitle: string;
    duration: number;
}
