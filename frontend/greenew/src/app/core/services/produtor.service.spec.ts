import { TestBed } from '@angular/core/testing';

import { ProdutorService } from './produtor.service';

describe('ProdutorService', () => {
  let service: ProdutorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProdutorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
