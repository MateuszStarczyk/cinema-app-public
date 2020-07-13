export class ReservedTicketKind {
  id: number;
  reservationId: string;
  name: string;
  price: number;
  qty: number;

  constructor(id: number, name: string, price: number) {
    this.id = id;
    this.reservationId = '';
    this.name = name;
    this.price = price;
    this.qty = 0;
  }
}
