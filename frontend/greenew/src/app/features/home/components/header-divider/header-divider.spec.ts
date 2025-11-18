import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderDivider } from './header-divider';

describe('HeaderDivider', () => {
  let component: HeaderDivider;
  let fixture: ComponentFixture<HeaderDivider>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderDivider]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderDivider);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
