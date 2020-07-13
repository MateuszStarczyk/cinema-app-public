import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageTicketsListComponent } from './manage-tickets-list.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';
import { ManageTicketsService } from '../../shared/services/manage-tickets.service';
import { of } from 'rxjs';
import { TicketKind } from 'src/app/shared/models/ticket-kind';

describe('ManageTicketsListComponent', () => {
  let component: ManageTicketsListComponent;
  let fixture: ComponentFixture<ManageTicketsListComponent>;
  let mockManageTicketService: ManageTicketsService;
  let spy: jasmine.Spy<any>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ManageTicketsListComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    mockManageTicketService = TestBed.inject(ManageTicketsService);
  });

  it('should create', () => {
    fixture = TestBed.createComponent(ManageTicketsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should add new row when add ticket clicked', () => {
    const initialTicketList = [
      {
        name: 'Adult',
        price: 25,
      },
    ];
    spy = spyOn(
      mockManageTicketService,
      'getAllManagedTickets'
    ).and.returnValue(
      of(initialTicketList)
    );
    fixture = TestBed.createComponent(ManageTicketsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('app-manage-tickets-list-item').length).toEqual(1);
    expect(JSON.stringify(component.tickets)).toEqual(JSON.stringify(initialTicketList));
    const addTicketButton = fixture.nativeElement.querySelector('.new-ticket-button');
    addTicketButton.click();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('app-manage-tickets-list-item').length).toEqual(2);
    expect(component.tickets.length).toEqual(2);
    expect(component.tickets[1]).toEqual(new TicketKind());
  });

  it('should display no tickets label', () => {
    spy = spyOn(
      mockManageTicketService,
      'getAllManagedTickets'
    ).and.returnValue(of([]));
    fixture = TestBed.createComponent(ManageTicketsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#no-tickets-admin')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('#no-tickets-admin').innerText).toEqual('No tickets found');
    expect(fixture.nativeElement.querySelector('mat-list')).toBeFalsy();
  });

  it('should display tickets list', () => {
    spy = spyOn(
      mockManageTicketService,
      'getAllManagedTickets'
    ).and.returnValue(
      of([
        {
          name: 'Adult',
          price: 25,
        },
        {
          name: 'Student',
          price: 15,
        },
      ])
    );
    fixture = TestBed.createComponent(ManageTicketsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('mat-list')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('#no-tickets-admin')).toBeFalsy();
  });

  it('should stop displaying no tickets label when adding new ticket', () => {
    spy = spyOn(
      mockManageTicketService,
      'getAllManagedTickets'
    ).and.returnValue(of([]));
    fixture = TestBed.createComponent(ManageTicketsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // initial state - no tickets
    expect(fixture.nativeElement.querySelector('#no-tickets-admin')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('#no-tickets-admin').innerText).toEqual('No tickets found');
    expect(fixture.nativeElement.querySelector('mat-list')).toBeFalsy();

    // add ticket
    const addTicketButton = fixture.nativeElement.querySelector('.new-ticket-button');
    addTicketButton.click();
    fixture.detectChanges();

    // new empty ticket item should appear
    expect(fixture.nativeElement.querySelector('mat-list')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('#no-tickets-admin')).toBeFalsy();
    expect(fixture.nativeElement.querySelectorAll('app-manage-tickets-list-item').length).toEqual(1);
    expect(component.tickets.length).toEqual(1);
    expect(component.tickets[0]).toEqual(new TicketKind());
  });
});
