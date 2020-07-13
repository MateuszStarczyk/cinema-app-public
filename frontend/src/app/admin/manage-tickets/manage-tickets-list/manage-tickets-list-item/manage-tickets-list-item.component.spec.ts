import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageTicketsListItemComponent } from './manage-tickets-list-item.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ManageTicketsListItemComponent', () => {
  let component: ManageTicketsListItemComponent;
  let fixture: ComponentFixture<ManageTicketsListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ManageTicketsListItemComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageTicketsListItemComponent);
    component = fixture.componentInstance;
    const ticketKind = {
      id: 1,
      name: 'Adult',
      price: 15,
    };
    component.ticketKind = ticketKind;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
