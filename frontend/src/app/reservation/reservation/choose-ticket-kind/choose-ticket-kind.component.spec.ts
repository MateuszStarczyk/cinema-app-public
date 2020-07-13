import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChooseTicketKindComponent } from './choose-ticket-kind.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';
import { GeneralInformationService } from 'src/app/shared/services/general-information/general-information.service';
import { of } from 'rxjs';

describe('ChooseTicketKindComponent', () => {
  let component: ChooseTicketKindComponent;
  let fixture: ComponentFixture<ChooseTicketKindComponent>;
  let mockGIService: GeneralInformationService;
  let spy: jasmine.Spy<any>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ChooseTicketKindComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    mockGIService = TestBed.inject(GeneralInformationService);
  });

  it('should create', () => {
    fixture = TestBed.createComponent(ChooseTicketKindComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should display no ticket kind label', () => {
    spy = spyOn(mockGIService, 'getAllTicketKinds').and.returnValue(of([]));
    fixture = TestBed.createComponent(ChooseTicketKindComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('#no-tickets')).toBeTruthy();
    expect(
      fixture.nativeElement.querySelector('#no-tickets').innerText
    ).toEqual('No ticket kinds found');
    expect(fixture.nativeElement.querySelector('mat-list')).toBeFalsy();
  });

  it('should display ticket kinds list', () => {
    spy = spyOn(mockGIService, 'getAllTicketKinds').and.returnValue(
      of([
        {
          id: 1,
          name: 'Adult',
          price: 10,
        },
      ])
    );
    fixture = TestBed.createComponent(ChooseTicketKindComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('mat-list')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('#no-tickets')).toBeFalsy();
  });
});
