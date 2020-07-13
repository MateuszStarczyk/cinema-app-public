import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { WhatsOnMovieItemComponent } from './whats-on-movie-item.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('WhatsOnMovieItemComponent', () => {
  let component: WhatsOnMovieItemComponent;
  let fixture: ComponentFixture<WhatsOnMovieItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ WhatsOnMovieItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WhatsOnMovieItemComponent);
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
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
