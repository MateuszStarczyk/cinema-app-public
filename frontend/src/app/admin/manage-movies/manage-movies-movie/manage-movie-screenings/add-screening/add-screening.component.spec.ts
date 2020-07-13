import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AddScreeningComponent } from './add-screening.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('AddScreeningComponent', () => {
  let component: AddScreeningComponent;
  let fixture: ComponentFixture<AddScreeningComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [AddScreeningComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddScreeningComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
