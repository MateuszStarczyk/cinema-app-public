import { TestBed, inject } from '@angular/core/testing';
import { TicketService } from './ticket.service';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('Service: Ticket', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      providers: [TicketService]
    });
  });

  it('should be created', inject([TicketService], (service: TicketService) => {
    expect(service).toBeTruthy();
  }));
});
