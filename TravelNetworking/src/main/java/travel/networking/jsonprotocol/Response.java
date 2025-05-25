package travel.networking.jsonprotocol;

import travel.model.Agency;
import travel.model.Flight;
import travel.model.Ticket;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private ResponseType type;
    private String errorMessage;
    private Agency agency;
    private Flight flight;
    private Ticket ticket;
    private Flight[] flights;

    public Response() {
    }

    public Flight[] getFlights() {
        return flights;
    }

    public void setFlights(Flight[] flights) {
        this.flights = flights;
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
//                ", errorMessage='" + errorMessage + '\'' +
                ", agency=" + agency +
                ", flight=" + flight +
                ", ticket=" + ticket +
                ", flights=" + flights +
                '}';
    }
}
