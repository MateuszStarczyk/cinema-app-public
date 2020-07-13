import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SeatsPlanComponent } from './seats-plan.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';
import { SeatsRow } from 'src/app/reservation/shared/models/seats-row';

describe('SeatsPlanComponent', () => {
  let component: SeatsPlanComponent;
  let fixture: ComponentFixture<SeatsPlanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [SeatsPlanComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SeatsPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('rows should be correctly generated', () => {
    // intial rows should be empty
    expect(component.rows.length).toEqual(0);
    component.seats = [
      {
        id: 1,
        seatNumber: 'A-1',
        isAvailable: false,
      },
      {
        id: 2,
        seatNumber: 'A-2',
        isAvailable: false,
      },
      {
        id: 3,
        seatNumber: 'B-1',
        isAvailable: true,
      },
      {
        id: 4,
        seatNumber: 'C-1',
        isAvailable: false,
      },
      {
        id: 5,
        seatNumber: 'C-2',
        isAvailable: true,
      },
      {
        id: 6,
        seatNumber: 'C-3',
        isAvailable: true,
      },
    ];
    fixture.detectChanges();

    // correct rows should be generated
    const expectedRows: SeatsRow[] = [
      {
        seats: [
          {
            id: 1,
            seatNumber: 'A-1',
            isAvailable: false,
          },
          {
            id: 2,
            seatNumber: 'A-2',
            isAvailable: false,
          },
        ],
        id: 'A',
      },
      {
        seats: [
          {
            id: 3,
            seatNumber: 'B-1',
            isAvailable: true,
          },
        ],
        id: 'B',
      },
      {
        seats: [
          {
            id: 4,
            seatNumber: 'C-1',
            isAvailable: false,
          },
          {
            id: 5,
            seatNumber: 'C-2',
            isAvailable: true,
          },
          {
            id: 6,
            seatNumber: 'C-3',
            isAvailable: true,
          },
        ],
        id: 'C',
      },
    ];

    expect(JSON.stringify(component.rows)).toEqual(
      JSON.stringify(expectedRows)
    );
  });

  it('seats should have correct colors and be disabled', () => {
    component.seats = [
      {
        id: 1,
        seatNumber: 'A-1',
        isAvailable: false,
      },
      {
        id: 2,
        seatNumber: 'A-2',
        isAvailable: true,
      },
      {
        id: 3,
        seatNumber: 'B-1',
        isAvailable: true,
      },
    ];
    component.selectedSeats = ['B-1'];
    fixture.detectChanges();

    // buttons should have correct classes and taken seats should be disabled
    expect(
      fixture.nativeElement.querySelector(
        'div > div:nth-child(1) > button:nth-child(2)'
      ).classList[5]
    ).toEqual('taken');
    expect(
      fixture.nativeElement.querySelector(
        'div > div:nth-child(1) > button:nth-child(2)'
      ).disabled
    ).toBeTrue();
    expect(
      fixture.nativeElement.querySelector(
        'div > div:nth-child(1) > button:nth-child(3)'
      ).classList[5]
    ).toEqual('available');
    expect(
      fixture.nativeElement.querySelector(
        'div > div:nth-child(1) > button:nth-child(3)'
      ).disabled
    ).toBeFalse();
    expect(
      fixture.nativeElement.querySelector(
        'div > div:nth-child(2) > button:nth-child(2)'
      ).classList[5]
    ).toEqual('chosen');
    expect(
      fixture.nativeElement.querySelector(
        'div > div:nth-child(2) > button:nth-child(2)'
      ).disabled
    ).toBeFalse();
  });
});
