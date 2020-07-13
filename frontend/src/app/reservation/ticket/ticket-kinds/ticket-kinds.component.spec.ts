import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TicketKindsComponent } from './ticket-kinds.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('TicketKindsComponent', () => {
  let component: TicketKindsComponent;
  let fixture: ComponentFixture<TicketKindsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ TicketKindsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TicketKindsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display ticket price and qty', () => {
    component.ticketKinds = [
      {
        id: 1,
        reservationId: '1',
        name: 'Student',
        price: 15.99,
        qty: 2,
      },
      {
        id: 2,
        reservationId: '1',
        name: 'Adult',
        price: 25.05,
        qty: 0,
      },
      {
        id: 3,
        reservationId: '1',
        name: 'Child',
        price: 12.3,
        qty: 1,
      },
    ];
    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('#ticket-total-qty').innerText).toEqual(
      'Seats (3 total)'
    );
    expect(fixture.nativeElement.querySelector('#ticket-total-price').innerText).toEqual(
      'Total price: 44.28 zł'
    );
  });
});
