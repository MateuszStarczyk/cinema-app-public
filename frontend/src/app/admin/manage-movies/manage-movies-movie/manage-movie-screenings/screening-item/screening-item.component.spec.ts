import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ScreeningItemComponent } from './screening-item.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ScreeningItemComponent', () => {
  let component: ScreeningItemComponent;
  let fixture: ComponentFixture<ScreeningItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ScreeningItemComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScreeningItemComponent);
    component = fixture.componentInstance;
    const screening = {
      id: 1,
      startDate: new Date(2020, 1, 12, 15, 44, 35),
      roomId: 1,
      movieId: 'movie'
    };

    component.screening = screening;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display screening', () => {
    fixture.detectChanges();
    const title = fixture.debugElement.query(By.css('.screening-field'));
    expect(title.nativeElement.innerText).toEqual('15:44 12-02-2020');
  });
});
