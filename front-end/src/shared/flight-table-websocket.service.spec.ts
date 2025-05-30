import { TestBed } from '@angular/core/testing';

import { FlightTableWebsocketService } from './flight-table-websocket.service';

describe('FlightTableWebsocketService', () => {
  let service: FlightTableWebsocketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlightTableWebsocketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
