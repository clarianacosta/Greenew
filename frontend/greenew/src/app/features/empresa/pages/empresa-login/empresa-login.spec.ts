import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaLogin } from './empresa-login';

describe('EmpresaLogin', () => {
  let component: EmpresaLogin;
  let fixture: ComponentFixture<EmpresaLogin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaLogin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpresaLogin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
