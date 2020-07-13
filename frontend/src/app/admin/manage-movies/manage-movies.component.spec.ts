import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageMoviesComponent } from './manage-movies.component';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ManageMoviesComponent', () => {
  let component: ManageMoviesComponent;
  let fixture: ComponentFixture<ManageMoviesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      declarations: [ManageMoviesComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageMoviesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
