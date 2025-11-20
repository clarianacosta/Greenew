import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEmpresas } from './admin-empresas';

describe('AdminEmpresas', () => {
  let component: AdminEmpresas;
  let fixture: ComponentFixture<AdminEmpresas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminEmpresas]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminEmpresas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
