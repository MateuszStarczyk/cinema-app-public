import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageMoviesListComponent } from './manage-movies-list.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';
import { ManageMoviesService } from '../../shared/services/manage-movies.service';
import { of } from 'rxjs';

describe('ManageMoviesListComponent', () => {
  let component: ManageMoviesListComponent;
  let fixture: ComponentFixture<ManageMoviesListComponent>;
  let mockManageMoviesService: ManageMoviesService;
  let spy: jasmine.Spy<any>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ManageMoviesListComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    mockManageMoviesService = TestBed.inject(ManageMoviesService);
  });

  it('should create', () => {
    fixture = TestBed.createComponent(ManageMoviesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should display no movies label', () => {
    spy = spyOn(mockManageMoviesService, 'getAllMovies').and.returnValue(
      of([])
    );
    fixture = TestBed.createComponent(ManageMoviesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#no-movies')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('#no-movies').innerText).toEqual(
      'No movies found'
    );
    expect(fixture.nativeElement.querySelector('mat-list')).toBeFalsy();
  });

  it('should display movies list', () => {
    spy = spyOn(mockManageMoviesService, 'getAllMovies').and.returnValue(
      of([
        {
          id: '1',
          title: 'Abc',
          duration: 90,
          posterFilename: null,
          trailerUrl: null,
          description: null,
        },
      ])
    );
    fixture = TestBed.createComponent(ManageMoviesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('mat-list')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('#no-movies')).toBeFalsy();
  });
});
