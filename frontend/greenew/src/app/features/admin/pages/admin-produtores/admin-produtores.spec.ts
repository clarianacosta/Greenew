import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminProdutores } from './admin-produtores';

describe('AdminProdutores', () => {
  let component: AdminProdutores;
  let fixture: ComponentFixture<AdminProdutores>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminProdutores]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminProdutores);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
