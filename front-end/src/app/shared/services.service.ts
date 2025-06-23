import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Flight } from '../model/flight';
import { Observable, Subject, firstValueFrom } from 'rxjs';
import { Airport } from '../model/airport';
import { CalendarDay } from '../model/calendar_day';

@Injectable({
  providedIn: 'root',
})
export class ServicesService {
  private SERVER_URL: string = 'http://localhost:8080/travel';

  private flightAddedSubject = new Subject<Flight>();
  public flightAdded = this.flightAddedSubject.asObservable();

  private flightUpdatedSubject = new Subject<Flight>();
  public flightUpdated = this.flightUpdatedSubject.asObservable();

  constructor(private client: HttpClient) {}

  getAllFlights(): Observable<Flight[]> {
    return this.client.get<Flight[]>(this.SERVER_URL + '/flights');
  }

  deleteFlight(flightId: number) {
    return this.client.delete(this.SERVER_URL + `/flights/${flightId}`);
  }

  notifyFlightAdded(flight: Flight) {
    this.flightAddedSubject.next(flight);
  }

  notifyFlightUpdated(flight: Flight) {
    this.flightUpdatedSubject.next(flight);
  }

  addFlight(flight: Omit<Flight, 'id'>): Observable<Flight> {
    return this.client.post<Flight>(this.SERVER_URL + '/flights', flight);
  }

  updateFlight(flight: Flight): Observable<Flight> {
    return this.client.put<Flight>(
      this.SERVER_URL + `/flights/${flight.id}`,
      flight
    );
  }

  async getAllAirpots(): Promise<Airport[]> {
    try {
      const response = await firstValueFrom(
        this.client.get<Airport[]>(`${this.SERVER_URL}/airports`)
      );
      return response;
    } catch (error) {
      console.error('Error fetching all airports: ', error);
      throw error;
    }
  }

  async getDestinationAirports(departureCode: string): Promise<Airport[]> {
    try {
      const response = await firstValueFrom(
        this.client.get<Airport[]>(
          `${this.SERVER_URL}/airports/destinations/${departureCode}`
        )
      );
      return response;
    } catch (error) {
      console.error('Error fetching destination airports:', error);
      throw error;
    }
  }

  async getTravelDays(
    departureAirport: Airport,
    destinationAirport: Airport,
    year: number,
    month: number
  ): Promise<CalendarDay[]> {
    const url = `${this.SERVER_URL}/flights?departureCode=${departureAirport.code}&destinationCode=${destinationAirport.code}&year=${year}&month=${month}`;

    try {
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch travel days');
      const data = await response.json();

      return data.map((item: any) => ({
        date: new Date(item.date),
        enabled: true,
        price: item.price,
      }));
    } catch (error) {
      console.error('Error fetching travel days:', error);
      return [];
    }
  }
}
