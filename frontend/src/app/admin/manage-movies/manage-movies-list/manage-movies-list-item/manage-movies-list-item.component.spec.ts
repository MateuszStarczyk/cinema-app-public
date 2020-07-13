import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ManageMoviesListItemComponent } from './manage-movies-list-item.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ManageMoviesListItemComponent', () => {
  let component: ManageMoviesListItemComponent;
  let fixture: ComponentFixture<ManageMoviesListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ManageMoviesListItemComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageMoviesListItemComponent);
    component = fixture.componentInstance;
    const movie = {
      id: 'movie-1',
      title: 'Movie 1',
      duration: 90,
      description: 'Lorem ipsum',
      posterFilename: 'poster1.jpg',
      trailerUrl: 'https://www.youtube.com/',
    };

    component.movie = movie;
    fixture.detectChanges();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display movie title', () => {
    fixture.detectChanges();
    const title = fixture.debugElement.query(By.css('mat-label'));
    expect(title.nativeElement.innerText).toEqual('Movie 1');
  });
});
