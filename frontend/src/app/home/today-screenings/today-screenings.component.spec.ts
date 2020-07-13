import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TodayScreeningsComponent } from './today-screenings.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('TodayScreeningsComponent', () => {
  let component: TodayScreeningsComponent;
  let fixture: ComponentFixture<TodayScreeningsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ TodayScreeningsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TodayScreeningsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
