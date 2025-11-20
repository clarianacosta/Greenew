import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaNavbar } from './empresa-navbar';

describe('EmpresaNavbar', () => {
  let component: EmpresaNavbar;
  let fixture: ComponentFixture<EmpresaNavbar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaNavbar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpresaNavbar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
