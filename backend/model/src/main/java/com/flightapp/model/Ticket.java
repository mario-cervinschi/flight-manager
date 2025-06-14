package com.flightapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer noOfSeats;

    @ElementCollection
    @CollectionTable(name = "ticket_tourist_names", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "tourist_name")
    private List<String> touristNames;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;
}
