import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { PendingTicketsComponent } from './pending-tickets.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('PendingTicketsComponent', () => {
  let component: PendingTicketsComponent;
  let fixture: ComponentFixture<PendingTicketsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [PendingTicketsComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PendingTicketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
