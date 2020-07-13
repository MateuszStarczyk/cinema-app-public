import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageMovieScreeningsComponent } from './manage-movie-screenings.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';
import { ManageMoviesService } from 'src/app/admin/shared/services/manage-movies.service';
import { of } from 'rxjs';

describe('ManageMovieScreeningsComponent', () => {
  let component: ManageMovieScreeningsComponent;
  let fixture: ComponentFixture<ManageMovieScreeningsComponent>;
  let mockManageMoviesService: ManageMoviesService;
  let spy: jasmine.Spy<any>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ManageMovieScreeningsComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    mockManageMoviesService = TestBed.inject(ManageMoviesService);
  });

  it('should create', () => {
    fixture = TestBed.createComponent(ManageMovieScreeningsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should display no screenings label', () => {
    spy = spyOn(
      mockManageMoviesService,
      'getAllScreeningsForMovie'
    ).and.returnValue(of([]));
    fixture = TestBed.createComponent(ManageMovieScreeningsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#no-screenings')).toBeTruthy();
    expect(
      fixture.nativeElement.querySelector('#no-screenings').innerText
    ).toEqual('No screenings found');
    expect(fixture.nativeElement.querySelector('mat-list')).toBeFalsy();
  });

  it('should display screenings list', () => {
    spy = spyOn(
      mockManageMoviesService,
      'getAllScreeningsForMovie'
    ).and.returnValue(
      of([
        {
          id: 1,
          startDate: new Date(),
          roomId: 1,
          movieId: '1',
        },
      ])
    );
    fixture = TestBed.createComponent(ManageMovieScreeningsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('mat-list')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('#no-screenings')).toBeFalsy();
  });
});
