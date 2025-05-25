package travel.networking.jsonprotocol;

import travel.model.Agency;
import travel.model.Flight;
import travel.model.Ticket;

import java.time.LocalDate;
import java.util.List;

public class Request {
    private RequestType type;
    private String destination;
    private String email;
    private String password;
    private LocalDate date;
    private Long aLong;
    private Agency agency;
    private Flight flight;
    private Ticket ticket;
    private Flight[] flights;

    public Request() {
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getaLong() {
        return aLong;
    }

    public void setaLong(Long aLong) {
        this.aLong = aLong;
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

    public Flight[] getFlights() {
        return flights;
    }

    public void setFlights(Flight[] flights) {
        this.flights = flights;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", destination='" + destination + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", date=" + date +
                ", aLong=" + aLong +
                ", agency=" + agency +
                ", flight=" + flight +
                ", ticket=" + ticket +
                ", flights=" + flights +
                '}';
    }
}
