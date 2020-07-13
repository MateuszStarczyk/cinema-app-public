import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageMoviesAddMovieComponent } from './manage-movies-add-movie.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ManageMoviesAddMovieComponent', () => {
  let component: ManageMoviesAddMovieComponent;
  let fixture: ComponentFixture<ManageMoviesAddMovieComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ManageMoviesAddMovieComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageMoviesAddMovieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
