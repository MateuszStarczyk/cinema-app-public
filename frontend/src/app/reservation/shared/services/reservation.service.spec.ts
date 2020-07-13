import { TestBed, inject } from '@angular/core/testing';
import { ReservationService } from './reservation.service';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('Service: Reservation', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      providers: [ReservationService]
    });
  });

  it('should be created', inject([ReservationService], (service: ReservationService) => {
    expect(service).toBeTruthy();
  }));
});
