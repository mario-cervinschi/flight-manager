import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageFlightsInterfaceComponent } from './manage-flights-interface.component';

describe('MainInterfaceComponent', () => {
  let component: ManageFlightsInterfaceComponent;
  let fixture: ComponentFixture<ManageFlightsInterfaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageFlightsInterfaceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageFlightsInterfaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
