package com.flightapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Airport departure;

    @ManyToOne
    private Airport destination;

    private LocalDate date;
    private LocalTime departureTime;
    private Double price;
}
