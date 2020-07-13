import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReservationComponent } from './reservation.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';
import { ActivatedRoute } from '@angular/router';

const hourInMs = 3600 * 1000;

describe('ReservationComponent', () => {
  let component: ReservationComponent;
  let fixture: ComponentFixture<ReservationComponent>;
  let route: ActivatedRoute;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ReservationComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    route = TestBed.inject(ActivatedRoute);
  });

  it('should create', () => {
    route.snapshot.data = {
      screening: {
        id: 1,
        startDate: new Date(),
        roomId: 2,
        movieId: 'abc',
        movieTitle: 'Movie 1',
        duration: 65,
        freeSeatsQuantity: 0,
      },
    };
    fixture = TestBed.createComponent(ReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should display tickets panel', () => {
    route.snapshot.data = {
      screening: {
        id: 1,
        startDate: new Date(new Date().getTime() + hourInMs),
        roomId: 2,
        movieId: 'abc',
        movieTitle: 'Movie 1',
        duration: 65,
        freeSeatsQuantity: 10,
      },
    };
    fixture = TestBed.createComponent(ReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(
      fixture.nativeElement.querySelector('#tickets-reservation-panel')
    ).toBeTruthy();
    expect(fixture.nativeElement.querySelector('#sold-out')).toBeFalsy();
    expect(fixture.nativeElement.querySelector('#outdated')).toBeFalsy();
  });

  it('should display outdated label', () => {
    route.snapshot.data = {
      screening: {
        id: 1,
        startDate: new Date(new Date().getTime() - hourInMs),
        roomId: 2,
        movieId: 'abc',
        movieTitle: 'Movie 1',
        duration: 65,
        freeSeatsQuantity: 10,
      },
    };
    fixture = TestBed.createComponent(ReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#outdated')).toBeTruthy();
    expect(
      fixture.nativeElement.querySelector('#tickets-reservation-panel')
    ).toBeFalsy();
    expect(fixture.nativeElement.querySelector('#sold-out')).toBeFalsy();
  });

  it('should display sold out label', () => {
    route.snapshot.data = {
      screening: {
        id: 1,
        startDate: new Date(new Date().getTime() + hourInMs),
        roomId: 2,
        movieId: 'abc',
        movieTitle: 'Movie 1',
        duration: 65,
        freeSeatsQuantity: 0,
      },
    };
    fixture = TestBed.createComponent(ReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#sold-out')).toBeTruthy();
    expect(
      fixture.nativeElement.querySelector('#tickets-reservation-panel')
    ).toBeFalsy();
    expect(fixture.nativeElement.querySelector('#outdated')).toBeFalsy();
  });

  it('buy button should block if seats not selected', () => {
    route.snapshot.data = {
      screening: {
        id: 1,
        startDate: new Date(new Date().getTime() + hourInMs),
        roomId: 2,
        movieId: 'abc',
        movieTitle: 'Movie 1',
        duration: 65,
        freeSeatsQuantity: 10,
      },
    };
    fixture = TestBed.createComponent(ReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // initial state (no tickets chosen - buy button disabled)
    expect(
      fixture.nativeElement.querySelector('.scr-buy-btn').disabled
    ).toBeTrue();

    // tickets selected but no seats selected - buy button disabled
    component.reservedTicketKindChanged([
      {
        id: 1,
        reservationId: '1',
        name: 'Adult',
        price: 15.99,
        qty: 2,
      },
    ]);

    fixture.detectChanges();
    expect(
      fixture.nativeElement.querySelector('.scr-buy-btn').disabled
    ).toBeTrue();

    // tickets selected but not all seats seleted - buy button disabled
    component.selectedSeats = ['A-1'];
    fixture.detectChanges();
    expect(
      fixture.nativeElement.querySelector('.scr-buy-btn').disabled
    ).toBeTrue();

    // tickets and seats seleted - buy button enabled
    component.selectedSeats = ['A-1', 'A-2'];
    fixture.detectChanges();
    expect(
      fixture.nativeElement.querySelector('.scr-buy-btn').disabled
    ).toBeFalse();
  });

  it('should calculate price', () => {
    route.snapshot.data = {
      screening: {
        id: 1,
        startDate: new Date(new Date().getTime() + hourInMs),
        roomId: 2,
        movieId: 'abc',
        movieTitle: 'Movie 1',
        duration: 65,
        freeSeatsQuantity: 10,
      },
    };
    fixture = TestBed.createComponent(ReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // initial state
    expect(fixture.nativeElement.querySelector('.src-price').innerText).toEqual(
      '0.00 zł'
    );

    // different tickets selected
    component.reservedTicketKindChanged([
      {
        id: 1,
        reservationId: '1',
        name: 'Student',
        price: 15.99,
        qty: 2,
      },
      {
        id: 2,
        reservationId: '1',
        name: 'Adult',
        price: 25.05,
        qty: 0,
      },
      {
        id: 3,
        reservationId: '1',
        name: 'Child',
        price: 12.3,
        qty: 1,
      },
    ]);

    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.src-price').innerText).toEqual(
      '44.28 zł'
    );

    // no tickets selected
    component.reservedTicketKindChanged([
      {
        id: 1,
        reservationId: '1',
        name: 'Student',
        price: 15.99,
        qty: 0,
      },
      {
        id: 2,
        reservationId: '1',
        name: 'Adult',
        price: 25.05,
        qty: 0,
      },
      {
        id: 3,
        reservationId: '1',
        name: 'Child',
        price: 12.3,
        qty: 0,
      },
    ]);

    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.src-price').innerText).toEqual(
      '0.00 zł'
    );
  });
});
