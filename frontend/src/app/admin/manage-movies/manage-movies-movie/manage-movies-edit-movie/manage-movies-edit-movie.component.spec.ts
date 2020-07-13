import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageMoviesEditMovieComponent } from './manage-movies-edit-movie.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ManageMoviesEditMovieComponent', () => {
  let component: ManageMoviesEditMovieComponent;
  let fixture: ComponentFixture<ManageMoviesEditMovieComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ManageMoviesEditMovieComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageMoviesEditMovieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
