import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { WhatsOnComponent } from './whats-on.component';
import { CommonTestModule } from '../shared/common-test/common-test.module';

describe('WhatsOnComponent', () => {
  let component: WhatsOnComponent;
  let fixture: ComponentFixture<WhatsOnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ WhatsOnComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WhatsOnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
