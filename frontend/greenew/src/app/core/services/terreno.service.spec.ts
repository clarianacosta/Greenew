import { TestBed } from '@angular/core/testing';

import { TerrenoService } from './terreno.service';

describe('TerrenoService', () => {
  let service: TerrenoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TerrenoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
