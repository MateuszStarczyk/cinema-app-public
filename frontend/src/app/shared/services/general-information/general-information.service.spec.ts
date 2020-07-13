import { TestBed, inject } from '@angular/core/testing';
import { GeneralInformationService } from './general-information.service';
import { CommonTestModule } from '../../common-test/common-test.module';

describe('Service: GeneralInformation', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommonTestModule],
      providers: [GeneralInformationService]
    });
  });

  it('should be created', inject([GeneralInformationService], (service: GeneralInformationService) => {
    expect(service).toBeTruthy();
  }));
});
