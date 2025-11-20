import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTerrenos } from './admin-terrenos';

describe('AdminTerrenos', () => {
  let component: AdminTerrenos;
  let fixture: ComponentFixture<AdminTerrenos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminTerrenos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTerrenos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
