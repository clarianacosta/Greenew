import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogoSimple } from './logo-simple';

describe('LogoSimple', () => {
  let component: LogoSimple;
  let fixture: ComponentFixture<LogoSimple>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogoSimple]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogoSimple);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
