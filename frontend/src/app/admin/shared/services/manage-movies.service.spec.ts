/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ManageMoviesService } from './manage-movies.service';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('Service: ManageMovies', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      providers: [ManageMoviesService],
    });
  });

  it('should be created', inject(
    [ManageMoviesService],
    (service: ManageMoviesService) => {
      expect(service).toBeTruthy();
    }
  ));
});
