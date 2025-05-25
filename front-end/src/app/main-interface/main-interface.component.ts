import { Component, OnInit, ViewChild } from '@angular/core';
import { Flight } from '../model/flight';
import { ServicesService } from '../services.service';
import { FormInputComponent } from './form-input/form-input.component';
import { UpdateFormComponent } from './update-form/update-form.component';

@Component({
  selector: 'app-main-interface',
  imports: [FormInputComponent, UpdateFormComponent],
  templateUrl: './main-interface.component.html',
  styleUrl: './main-interface.component.css'
})
export class MainInterfaceComponent implements OnInit{

  flights: Flight[] = [];

  @ViewChild('updateForm') updateFormComponent!: UpdateFormComponent;
  @ViewChild(FormInputComponent) formInput! : FormInputComponent;

  constructor(private service: ServicesService){
  }

  ngOnInit(): void {
    this.refreshFlightsList();

    this.service.flightAdded.subscribe((newFlight: Flight) => {
      this.flights.push(newFlight);
    })

    this.service.flightUpdated.subscribe((updatedFlight: Flight) => {
      const index = this.flights.findIndex(flight => flight.id === updatedFlight.id);
      if (index !== -1) {
        this.flights[index] = updatedFlight;
      }
    })
  }

  refreshFlightsList(){
    this.service.getAllFlights().subscribe(
      {next: (flights : Flight[]) => {
        this.flights = flights;
        console.log(flights);
      },
      error: (error) => {
        console.log(error);
      }
    }
    )
  }

  handleDeletion(flightId: number): void{
    this.service.deleteFlight(flightId).subscribe(() => {
      this.refreshFlightsList();
    });
  }

  handleSubmit(){
    const fg : any = this.formInput.retrieveFormData();
    if (fg !== null){
      const newFlight = {
      destination: fg.get('destinationForm')?.value || '',
      timeOfDeparture: fg.get('dateForm')?.value || '',
      airportName: fg.get('airportNameForm')?.value || '',
      availableSeats: Number(fg.get('seatNumbersForm')?.value)
    };

      this.service.addFlight(newFlight).subscribe((fl : Flight) => {
        fg.reset(); this.service.notifyFlightAdded(fl)
      })
    }  
  }

  handleUpdate(flight: Flight): void{
    this.updateFormComponent.flight = flight;
    this.updateFormComponent.openModal();
  }

}
