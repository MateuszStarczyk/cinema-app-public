import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TicketsHistoryListComponent } from './tickets-history-list.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';
import { ActivatedRoute } from '@angular/router';

describe('TicketsHistoryListComponent', () => {
  let component: TicketsHistoryListComponent;
  let fixture: ComponentFixture<TicketsHistoryListComponent>;
  let route: ActivatedRoute;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [TicketsHistoryListComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    route = TestBed.inject(ActivatedRoute);
  });

  it('should create', () => {
    fixture = TestBed.createComponent(TicketsHistoryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should display no tickets history label', () => {
    route.snapshot.data = {
      tickets: [
        {
          number: '12345',
          screeningId: 1,
          customerName: 'Kinga Buczkowska',
          customerEmail: 'tt@testing.com',
          customerId: 1,
          startDate: new Date(),
          roomId: 1,
          movieId: 'abc',
          movieTitle: 'Movie 1',
          duration: 60,
          selectedSeats: ['A1', 'A2'],
          ticketKinds: [
            {
              id: 1,
              reservationId: '1234',
              name: 'Adult',
              price: 25,
              qty: 3,
            },
            {
              id: 2,
              reservationId: '1234',
              name: 'Student',
              price: 15,
              qty: 1,
            },
          ],
        },
      ],
    };

    fixture = TestBed.createComponent(TicketsHistoryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(
      fixture.nativeElement.querySelector('#no-history-tickets')
    ).toBeFalsy();
  });

  it('should display date filter', () => {
    route.snapshot.data = {
      tickets: [],
    };

    fixture = TestBed.createComponent(TicketsHistoryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(
      fixture.nativeElement.querySelector('#no-history-tickets')
    ).toBeTruthy();
    expect(
      fixture.nativeElement.querySelector('#no-history-tickets').innerText
    ).toEqual('No tickets found');
  });
});
