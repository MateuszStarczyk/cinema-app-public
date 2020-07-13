import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChooseSeatsComponent } from './choose-seats.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';
import { ReservationService } from '../../shared/services/reservation.service';
import { of } from 'rxjs/internal/observable/of';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

describe('ChooseSeatsComponent', () => {
  let component: ChooseSeatsComponent;
  let fixture: ComponentFixture<ChooseSeatsComponent>;
  let mockReservationService: ReservationService;
  let spy: jasmine.Spy<any>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ChooseSeatsComponent],
      providers: [
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            seatsQty: 3,
            selectedSeats: ['A-1'],
            screeningId: 123,
          },
        },
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    mockReservationService = TestBed.inject(ReservationService);

    // close any opened alerts
    const sweetAlertContainerSelector =
      'body > div.swal2-container.swal2-center.swal2-backdrop-show';
    const sweetAlertOkButtonSelector =
      'body > div.swal2-container.swal2-center.swal2-backdrop-show > div > div.swal2-actions > button.swal2-confirm.swal2-styled';
    if (document.querySelector(sweetAlertContainerSelector)) {
      const button: any = document.querySelector(sweetAlertOkButtonSelector);
      button.click();
    }
  });

  it('should create', () => {
    fixture = TestBed.createComponent(ChooseSeatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('initial state: should keep seats', () => {
    spy = spyOn(
      mockReservationService,
      'getScreeningSeatAvailability'
    ).and.returnValue(
      of([
        {
          id: 123,
          seatNumber: 'A-1',
          isAvailable: true,
        },
      ])
    );
    fixture = TestBed.createComponent(ChooseSeatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    // seats qty shouldn't change
    expect(component.seatsQty).toEqual(3);
    // all seats should be filled
    expect(component.allSeats).toEqual([
      {
        id: 123,
        seatNumber: 'A-1',
        isAvailable: true,
      },
    ]);
    // selected seats shouldn't change
    expect(component.selectedSeats).toEqual(['A-1']);
    expect(fixture.nativeElement.querySelector('mat-label').innerText).toEqual(
      '1 / 3 chosen'
    );
    // any alerts shouldn't be shown
    expect(
      document.querySelector(
        'body > div.swal2-container.swal2-center.swal2-backdrop-show'
      )
    ).toBeFalsy();
  });

  it('initial state: should clear seats - case no seats', () => {
    spy = spyOn(
      mockReservationService,
      'getScreeningSeatAvailability'
    ).and.returnValue(of([]));
    fixture = TestBed.createComponent(ChooseSeatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
    // seats qty shouldn't change
    expect(component.seatsQty).toEqual(3);
    // all seats should be filled
    expect(component.allSeats.length).toEqual(0);
    // selected seats should be cleared
    expect(component.selectedSeats.length).toEqual(0);
    expect(fixture.nativeElement.querySelector('mat-label').innerText).toEqual(
      '0 / 3 chosen'
    );
    // dedicated alert should be shown
    expect(
      document.querySelector(
        'body > div.swal2-container.swal2-center.swal2-backdrop-show'
      )
    ).toBeTruthy();
    expect(document.querySelector('#swal2-content').innerHTML).toEqual(
      'Your selected seats have been taken! Choose new seats.'
    );
  });

  it('initial state: should clear seats - case not available seats', () => {
    spy = spyOn(
      mockReservationService,
      'getScreeningSeatAvailability'
    ).and.returnValue(
      of([
        {
          id: 123,
          seatNumber: 'A-1',
          isAvailable: false,
        },
      ])
    );
    fixture = TestBed.createComponent(ChooseSeatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    // seats qty shouldn't change
    expect(component.seatsQty).toEqual(3);
    // all seats should be filled
    expect(component.allSeats).toEqual([
      {
        id: 123,
        seatNumber: 'A-1',
        isAvailable: false,
      },
    ]);
    // selected seats should be cleared
    expect(component.selectedSeats.length).toEqual(0);
    expect(fixture.nativeElement.querySelector('mat-label').innerText).toEqual(
      '0 / 3 chosen'
    );
    // dedicated alert should be shown
    expect(
      document.querySelector(
        'body > div.swal2-container.swal2-center.swal2-backdrop-show'
      )
    ).toBeTruthy();
    expect(document.querySelector('#swal2-content').innerHTML).toEqual(
      'Your selected seats have been taken! Choose new seats.'
    );
  });

  it('should select seats', () => {
    spy = spyOn(
      mockReservationService,
      'getScreeningSeatAvailability'
    ).and.returnValue(
      of([
        {
          id: 1,
          seatNumber: 'A-1',
          isAvailable: true,
        },
        {
          id: 2,
          seatNumber: 'A-2',
          isAvailable: true,
        },
        {
          id: 3,
          seatNumber: 'A-3',
          isAvailable: true,
        },
        {
          id: 4,
          seatNumber: 'A-4',
          isAvailable: true,
        },
        {
          id: 5,
          seatNumber: 'A-5',
          isAvailable: false,
        },
      ])
    );
    fixture = TestBed.createComponent(ChooseSeatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // intial state
    expect(component.selectedSeats).toEqual(['A-1']);

    // select two other seats
    let seatButton = fixture.nativeElement.querySelector(
      'mat-dialog-content > app-seats-plan > div > div:nth-child(1) > button:nth-child(3)'
    );
    seatButton.click();
    seatButton = fixture.nativeElement.querySelector(
      'mat-dialog-content > app-seats-plan > div > div:nth-child(1) > button:nth-child(4)'
    );
    seatButton.click();
    expect(component.selectedSeats).toEqual(['A-1', 'A-2', 'A-3']);

    // try to select one seat more than limit
    seatButton = fixture.nativeElement.querySelector(
      'mat-dialog-content > app-seats-plan > div > div:nth-child(1) > button:nth-child(5)'
    );
    seatButton.click();
    expect(component.selectedSeats).toEqual(['A-1', 'A-2', 'A-3']);

    // deselect a seat
    seatButton = fixture.nativeElement.querySelector(
      'mat-dialog-content > app-seats-plan > div > div:nth-child(1) > button:nth-child(3)'
    );
    seatButton.click();
    expect(component.selectedSeats).toEqual(['A-1', 'A-3']);

    // try select taken seat
    seatButton = fixture.nativeElement.querySelector(
      'mat-dialog-content > app-seats-plan > div > div:nth-child(1) > button:nth-child(6)'
    );
    seatButton.click();
    expect(component.selectedSeats).toEqual(['A-1', 'A-3']);
  });
});
