import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ScreeningsListComponent } from './screenings-list.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ScreeningsListComponent', () => {
  let component: ScreeningsListComponent;
  let fixture: ComponentFixture<ScreeningsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ScreeningsListComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScreeningsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display no screenings label', () => {
    component.screenings = [];
    component.isLoading = false;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#no-screenings')).toBeTruthy();
    expect(
      fixture.nativeElement.querySelector('#no-screenings').innerText
    ).toEqual('No screenings found');
    expect(fixture.nativeElement.querySelector('mat-list')).toBeFalsy();
    expect(fixture.nativeElement.querySelector('mat-spinner')).toBeFalsy();
  });

  it('should display screenings', () => {
    component.screenings = [
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
    ];
    component.isLoading = false;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('mat-list')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('#no-screenings')).toBeFalsy();
    expect(fixture.nativeElement.querySelector('mat-spinner')).toBeFalsy();
  });

  it('should display loading', () => {
    component.isLoading = true;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('mat-spinner')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('mat-list')).toBeFalsy();
    expect(fixture.nativeElement.querySelector('#no-screenings')).toBeFalsy();
  });
});
