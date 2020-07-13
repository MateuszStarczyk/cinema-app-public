import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ScreeningsListItemComponent } from './screenings-list-item.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

const hourInMs = 3600 * 1000;

describe('ScreeningsListItemComponent', () => {
  let component: ScreeningsListItemComponent;
  let fixture: ComponentFixture<ScreeningsListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ ScreeningsListItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScreeningsListItemComponent);
    component = fixture.componentInstance;
    const screening = {
      title: 'Movie 1',
      id: '1',
      posterFilename: 'movie1.jpg',
      duration: 60,
      description: '',
      trailerUrl: '',
      screenings: [
        {
          id: 1,
          movieId: '1',
          roomId: 2,
          startDate: new Date(new Date().getTime() + hourInMs),
        },
        {
          id: 2,
          movieId: '1',
          roomId: 2,
          startDate: new Date(new Date().getTime() + 2 * hourInMs),
        },
        {
          id: 3,
          movieId: '1',
          roomId: 2,
          startDate: new Date(new Date().getTime() - hourInMs),
        },
      ],
    };
    component.screening = screening;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should disable passed screenings', () => {
    expect(fixture.nativeElement.querySelector('div > button:nth-child(2)').disabled).toBeFalse();
    expect(fixture.nativeElement.querySelector('div > button:nth-child(3)').disabled).toBeFalse();
    expect(fixture.nativeElement.querySelector('div > button:nth-child(4)').disabled).toBeTrue();
  });
});
