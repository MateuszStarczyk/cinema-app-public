import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TicketComponent } from './ticket.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';
import { ActivatedRoute } from '@angular/router';

describe('TicketComponent', () => {
  let component: TicketComponent;
  let fixture: ComponentFixture<TicketComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ TicketComponent ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              data: {
                ticket: {
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
                      ticketKindId: 1,
                      name: 'Adult',
                      price: 25,
                      qty: 3,
                    },
                    {
                      id: 2,
                      reservationId: '1234',
                      ticketKindId: 2,
                      name: 'Student',
                      price: 15,
                      qty: 1,
                    },
                  ],
                },
              },
            },
          },
        },
      ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TicketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
