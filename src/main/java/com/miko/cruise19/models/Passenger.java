package com.miko.cruise19.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private int roomNumber;
    @Column
    @Enumerated
    private Status status;
    @ManyToOne
    @JsonBackReference
    private CruiseShip cruiseShip;

    public Passenger() {}

    public Passenger(String name, int roomNumber, Status status, CruiseShip cruiseShip) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.status = status;
        this.cruiseShip = cruiseShip;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CruiseShip getCruiseShip() {
        return cruiseShip;
    }

    public void setCruiseShip(CruiseShip cruiseShip) {
        this.cruiseShip = cruiseShip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return id == passenger.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
