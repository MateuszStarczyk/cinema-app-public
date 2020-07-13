import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageTicketsComponent } from './manage-tickets.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ManageTicketsComponent', () => {
  let component: ManageTicketsComponent;
  let fixture: ComponentFixture<ManageTicketsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ManageTicketsComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageTicketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
