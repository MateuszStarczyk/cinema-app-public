import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ScreeningsComponent } from './screenings.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';
import { ReservationService } from '../shared/services/reservation.service';
import { of } from 'rxjs';

describe('ScreeningsComponent', () => {
  let component: ScreeningsComponent;
  let fixture: ComponentFixture<ScreeningsComponent>;
  let mockReservationService: ReservationService;
  let spy: jasmine.Spy<any>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ScreeningsComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    mockReservationService = TestBed.inject(ReservationService);
  });

  it('should create', () => {
    fixture = TestBed.createComponent(ScreeningsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should change screenings after date changed', () => {
    const initialScreenings = [
      {
        id: '1',
        title: 'Abc',
        duration: 90,
        posterFilename: null,
        trailerUrl: null,
        description: null,
        screenings: [
          {
            id: 1,
            startDate: new Date(),
            roomId: 1,
            movieId: '1',
          },
        ],
      },
      {
        id: '2',
        title: 'Def',
        duration: 120,
        posterFilename: null,
        trailerUrl: null,
        description: null,
        screenings: [
          {
            id: 2,
            startDate: new Date(),
            roomId: 2,
            movieId: '2',
          },
        ],
      },
    ];

    spy = spyOn(
      mockReservationService,
      'getAllMovieScreeningsForDate'
    ).and.returnValue(of(initialScreenings));
    fixture = TestBed.createComponent(ScreeningsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(JSON.stringify(component.screenings)).toEqual(
      JSON.stringify(initialScreenings)
    );

    // change date event
    const reloadedScreenings = [
      {
        id: '1',
        title: 'Abc',
        duration: 90,
        posterFilename: null,
        trailerUrl: null,
        description: null,
        screenings: [
          {
            id: 3,
            startDate: new Date(),
            roomId: 1,
            movieId: '1',
          },
        ],
      },
      {
        id: '2',
        title: 'Def',
        duration: 120,
        posterFilename: null,
        trailerUrl: null,
        description: null,
        screenings: [
          {
            id: 4,
            startDate: new Date(),
            roomId: 2,
            movieId: '2',
          },
          {
            id: 5,
            startDate: new Date(),
            roomId: 1,
            movieId: '2',
          },
        ],
      },
    ];

    spy.and.returnValue(of(reloadedScreenings));
    component.dateChanged(new Date());
    fixture.detectChanges();
    expect(JSON.stringify(component.screenings)).toEqual(
      JSON.stringify(reloadedScreenings)
    );
  });
});
