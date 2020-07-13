import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TicketsHistoryComponent } from './tickets-history.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('TicketsHistoryComponent', () => {
  let component: TicketsHistoryComponent;
  let fixture: ComponentFixture<TicketsHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [TicketsHistoryComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TicketsHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
