import { Component, OnInit, ViewChild } from '@angular/core';
import { Flight } from '../model/flight';
import { ServicesService } from '../../shared/services.service';
import { FormInputComponent } from './form-input/form-input.component';
import { UpdateFormComponent } from './update-form/update-form.component';
import { Subscription } from 'rxjs';
import { FlightTableWebsocketService } from '../../shared/flight-table-websocket.service';

@Component({
  selector: 'app-main-interface',
  imports: [FormInputComponent, UpdateFormComponent],
  templateUrl: './main-interface.component.html',
  styleUrl: './main-interface.component.css'
})
export class MainInterfaceComponent implements OnInit{

  flights: Flight[] = [];
  private wsSubscription?: Subscription;

  @ViewChild('updateForm') updateFormComponent!: UpdateFormComponent;
  @ViewChild(FormInputComponent) formInput! : FormInputComponent;

  constructor(private service: ServicesService, private flightWsService: FlightTableWebsocketService){
  }

  ngOnInit(): void {
    this.refreshFlightsList();

    this.flightWsService.connect();

    this.wsSubscription = this.flightWsService.flights$.subscribe(flights => {
      this.flights = flights;  
      console.log('Received flights update from WebSocket:', flights);
    });

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
