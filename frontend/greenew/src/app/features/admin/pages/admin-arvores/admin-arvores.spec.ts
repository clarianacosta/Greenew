import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminArvores } from './admin-arvores';

describe('AdminArvores', () => {
  let component: AdminArvores;
  let fixture: ComponentFixture<AdminArvores>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminArvores]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminArvores);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
