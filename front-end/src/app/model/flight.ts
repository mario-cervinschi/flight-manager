export class Flight{
    id!: number;
    destination!: string;
    timeOfDeparture!: string;
    airportName!: string;
    availableSeats!: number;

    constructor(id: number, destination: string, timeOfDeparture: string, airportName: string, availableSeats: number){
        this.id = id;
        this.destination = destination;
        this.timeOfDeparture = timeOfDeparture;
        this.airportName = airportName;
        this.availableSeats = availableSeats;
    }
}