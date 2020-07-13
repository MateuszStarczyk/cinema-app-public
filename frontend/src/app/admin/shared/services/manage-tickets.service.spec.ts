import { TestBed } from '@angular/core/testing';

import { ManageTicketsService } from './manage-tickets.service';
import { CommonTestModule } from 'src/app/shared/common-test/common-test.module';

describe('ManageTicketsService', () => {
  let service: ManageTicketsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule]
    });
    service = TestBed.inject(ManageTicketsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
