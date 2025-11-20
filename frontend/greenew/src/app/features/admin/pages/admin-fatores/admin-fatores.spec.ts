import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFatores } from './admin-fatores';

describe('AdminFatores', () => {
  let component: AdminFatores;
  let fixture: ComponentFixture<AdminFatores>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFatores]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFatores);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
