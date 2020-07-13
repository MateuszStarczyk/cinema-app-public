import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ManageMovieFormComponent } from './manage-movie-form.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ManageMovieFormComponent', () => {
  let component: ManageMovieFormComponent;
  let fixture: ComponentFixture<ManageMovieFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ManageMovieFormComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageMovieFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fill field', () => {
    fixture.detectChanges();
    const input = fixture.debugElement.query(By.css('input'));
    input.nativeElement.value = 'movie-1';
    expect(input.nativeElement.value).toEqual('movie-1');
  });
});
