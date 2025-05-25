import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ServicesService } from '../../services.service';
import { Flight } from '../../model/flight';

@Component({
  selector: 'app-form-input',
  imports: [ReactiveFormsModule],
  templateUrl: './form-input.component.html',
  styleUrl: './form-input.component.css'
})
export class FormInputComponent {
  fg = new FormGroup({
    destinationForm: new FormControl('', Validators.required),
    airportNameForm: new FormControl('', Validators.required),
    dateForm: new FormControl('', Validators.required),
    seatNumbersForm: new FormControl('', [Validators.required, Validators.min(1)])
  })

  constructor(private service: ServicesService){}

  handleUpdate(flight: Flight) : boolean{
    if(this.fg.valid){
      const newFlight = {
        id: flight.id,
        destination: this.fg.get('destinationForm')?.value || '',
        timeOfDeparture: this.fg.get('dateForm')?.value || '',
        airportName: this.fg.get('airportNameForm')?.value || '',
        availableSeats: Number(this.fg.get('seatNumbersForm')?.value)
      };

      this.service.updateFlight(newFlight).subscribe((fl : Flight) => {
        this.fg.reset(); this.service.notifyFlightUpdated(fl);
      })
      return true;
    }
    else{
      this.fg.markAllAsTouched();
      return false;
    }
  }

  handleSubmit(){
    this.fg.markAllAsTouched();

    if(this.fg.valid){
      const newFlight = {
        destination: this.fg.get('destinationForm')?.value || '',
        timeOfDeparture: this.fg.get('dateForm')?.value || '',
        airportName: this.fg.get('airportNameForm')?.value || '',
        availableSeats: Number(this.fg.get('seatNumbersForm')?.value)
      };

      this.service.addFlight(newFlight).subscribe((fl : Flight) => {
        this.fg.reset(); this.service.notifyFlightAdded(fl)
      })
    }
  }
}
