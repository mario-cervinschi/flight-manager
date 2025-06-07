import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FlightTableWebsocketService {

  private socket!: WebSocket;
  private flightsSubject = new BehaviorSubject<any[]>([]);
  public flights$ = this.flightsSubject.asObservable();

  constructor(private ngZone: NgZone) { }

  connect() {
    this.socket = new WebSocket('ws://localhost:8080/flightws');

    this.socket.onopen = () => {
      console.log('WebSocket connection opened');
    };

    this.socket.onmessage = (event) => {
      this.ngZone.run(() => {
        try {
          const flights = JSON.parse(event.data);
          this.flightsSubject.next(flights);
        } catch (e) {
          console.error('Error parsing WebSocket message', e);
        }
      });
    };

    this.socket.onclose = (event) => {
      console.log('WebSocket connection closed', event);
    };

    this.socket.onerror = (event) => {
      console.error('WebSocket error', event);
    };
  }

  sendMessage(message: any) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(JSON.stringify(message));
    }
  }

  disconnect() {
    if (this.socket) {
      this.socket.close();
    }
  }
}
