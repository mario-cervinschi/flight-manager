import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Flight } from '../model/flight';
import { Observable, Subject, firstValueFrom } from 'rxjs';
import { Airport } from '../model/airport';

@Injectable({
  providedIn: 'root'
})
export class ServicesService {

  private SERVER_URL: string = "http://localhost:8080/travel/flights"

  private flightAddedSubject = new Subject<Flight>();
  public flightAdded = this.flightAddedSubject.asObservable();

  private flightUpdatedSubject = new Subject<Flight>();
  public flightUpdated = this.flightUpdatedSubject.asObservable();

  constructor(private client: HttpClient) { }

  getAllFlights() : Observable<Flight[]>{
    return this.client.get<Flight[]>(this.SERVER_URL);
  }

  deleteFlight(flightId: number){
    return this.client.delete(this.SERVER_URL + `/${flightId}`);
  }

  notifyFlightAdded(flight: Flight){
    this.flightAddedSubject.next(flight);
  }

  notifyFlightUpdated(flight: Flight){
    this.flightUpdatedSubject.next(flight);
  }

  addFlight(flight: Omit<Flight, 'id'>) : Observable<Flight> {
    return this.client.post<Flight>(this.SERVER_URL, flight);
  }

  updateFlight(flight: Flight): Observable<Flight>{
    return this.client.put<Flight>(this.SERVER_URL + `/${flight.id}`, flight);
  }

  async getDestinationAirports(departureCode: string): Promise<Airport[]> {
    try {
      const response = await firstValueFrom(
        this.client.get<Airport[]>(`${this.SERVER_URL}/destinations/${departureCode}`)
      );
      return response;
    } catch (error) {
      console.error('Error fetching destination airports:', error);
      throw error;
    }
  }

}
