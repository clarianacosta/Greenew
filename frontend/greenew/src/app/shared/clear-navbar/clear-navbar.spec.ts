import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClearNavbar } from './clear-navbar';

describe('ClearNavbar', () => {
  let component: ClearNavbar;
  let fixture: ComponentFixture<ClearNavbar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClearNavbar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClearNavbar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
