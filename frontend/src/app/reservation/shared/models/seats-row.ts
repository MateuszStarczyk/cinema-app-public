import { ScreeningSeat } from './screening-seat';

export class SeatsRow {
  id: string;
  seats: ScreeningSeat[] = [];

  constructor(id: string) {
    this.id = id;
  }
}
