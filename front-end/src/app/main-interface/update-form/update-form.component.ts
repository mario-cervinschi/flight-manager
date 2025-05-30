import { CommonModule } from '@angular/common';
import { Component, Input, ViewChild } from '@angular/core';
import { Flight } from '../../model/flight';
import { FormInputComponent } from "../form-input/form-input.component";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ServicesService } from '../../../shared/services.service';

declare var bootstrap: any;

@Component({
  selector: 'app-update-form',
  imports: [CommonModule, ReactiveFormsModule, FormInputComponent],
  templateUrl: './update-form.component.html',
  styleUrl: './update-form.component.css'
})
export class UpdateFormComponent {
  @Input() flight!: Flight;
  @ViewChild(FormInputComponent) formInput!: FormInputComponent;

  visible = false;

  constructor(private service: ServicesService){}

  openModal(): void {
    
    this.visible = true;
  }

  close(): void {
    this.visible = false;
  }

  submitUpdate(): void {
    const fg = this.formInput.retrieveFormData();
    if(fg !== null){
      const newFlight = {
        id: this.flight.id,
        destination: fg.get('destinationForm')?.value || '',
        timeOfDeparture: fg.get('dateForm')?.value || '',
        airportName: fg.get('airportNameForm')?.value || '',
        availableSeats: Number(fg.get('seatNumbersForm')?.value)
      };

      this.service.updateFlight(newFlight).subscribe((fl : Flight) => {
        fg.reset(); this.service.notifyFlightUpdated(fl);
      })
    }
  }
}
