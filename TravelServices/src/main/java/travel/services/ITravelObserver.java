package travel.services;

import travel.model.Ticket;

public interface ITravelObserver {
    void boughtTicket(Ticket ticket);
}
