import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NoPermissionsComponent } from './no-permissions.component';
import { CommonTestModule } from '../common-test/common-test.module';

describe('NoPermissionsComponent', () => {
  let component: NoPermissionsComponent;
  let fixture: ComponentFixture<NoPermissionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [NoPermissionsComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NoPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display no permissions', () => {
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('h2').innerText).toEqual(
      'No permissions to access this page!'
    );
  });
});
