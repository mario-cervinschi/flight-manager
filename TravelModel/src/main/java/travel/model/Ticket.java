package travel.model;

import java.util.List;
import java.util.Objects;

public class Ticket extends Entity<Long>{
    private final Integer noOfSeats;
    private final List<String> touristNames;
    private Flight flight;
    private Agency agency;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(noOfSeats, ticket.noOfSeats) && Objects.equals(touristNames, ticket.touristNames) && Objects.equals(flight, ticket.flight) && Objects.equals(agency, ticket.agency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), noOfSeats, touristNames, flight, agency);
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Ticket(Integer noOfSeats, List<String> touristNames, Flight flight, Agency agency) {
        this.noOfSeats = noOfSeats;
        this.touristNames = touristNames;
        this.flight = flight;
        this.agency = agency;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public List<String> getTouristNames() {
        return touristNames;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "noOfSeats=" + noOfSeats +
                ", touristNames=" + touristNames +
                ", flight=" + flight +
                ", agency=" + agency +
                '}';
    }
}
