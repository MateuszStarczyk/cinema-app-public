import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {MyTicketsItemComponent} from './my-tickets-item.component';
import {CommonTestModule} from 'src/app/shared/common-test/common-test.module';

describe('MyTicketsItemComponent', () => {
  let component: MyTicketsItemComponent;
  let fixture: ComponentFixture<MyTicketsItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [MyTicketsItemComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyTicketsItemComponent);
    component = fixture.componentInstance;
    component.ticket =  {
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
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
