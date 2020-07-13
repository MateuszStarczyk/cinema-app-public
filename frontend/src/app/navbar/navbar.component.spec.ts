import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NavbarComponent } from './navbar.component';
import { CommonTestModule } from '../shared/common-test/common-test.module';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [NavbarComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should compile', () => {
    expect(component).toBeTruthy();
  });

  it('should have title', () => {
    fixture.detectChanges();
    expect(
      fixture.nativeElement.querySelector('.navbar-app-title').textContent
    ).toEqual(' Cinema.com ');
  });

  it('should have 5 buttons', () => {
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('button').length).toEqual(5);
  });
});
