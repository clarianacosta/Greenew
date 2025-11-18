import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogoText } from './logo-text';

describe('LogoText', () => {
  let component: LogoText;
  let fixture: ComponentFixture<LogoText>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogoText]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogoText);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
