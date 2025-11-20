import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NovoRelatorio } from './novo-relatorio';

describe('NovoRelatorio', () => {
  let component: NovoRelatorio;
  let fixture: ComponentFixture<NovoRelatorio>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NovoRelatorio]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NovoRelatorio);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
