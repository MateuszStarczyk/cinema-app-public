import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChooseTicketKindItemComponent } from './choose-ticket-kind-item.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ChooseTicketKindItemComponent', () => {
  let component: ChooseTicketKindItemComponent;
  let fixture: ComponentFixture<ChooseTicketKindItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ChooseTicketKindItemComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseTicketKindItemComponent);
    component = fixture.componentInstance;
    const reservedTicketKind = {
      id: 1,
      reservationId: '123',
      name: 'Adult',
      price: 25,
      qty: 1,
    };
    component.reservedTicketKind = reservedTicketKind;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should calculate available quantity', () => {
    // case available + reserved < default
    component.availableQty = 2;
    fixture.detectChanges();
    expect(component.quantities).toEqual([0, 1, 2, 3]);

    // case available + reserved > default
    component.availableQty = 25;
    fixture.detectChanges();
    expect(component.quantities).toEqual([0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]);

    // case available and no reserved < default
    component.reservedTicketKind = {
      id: 1,
      reservationId: '123',
      name: 'Adult',
      price: 25,
      qty: 0,
    };
    component.availableQty = 2;
    fixture.detectChanges();
    expect(component.quantities).toEqual([0, 1, 2]);
  });
});
