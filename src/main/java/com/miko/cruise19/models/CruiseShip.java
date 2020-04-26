package com.miko.cruise19.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cruiseships")
public class CruiseShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @Column
    private int numOfDecks;
    @Column
    private int numOfPools;
    @Column
    private int numOfRooms;
    @OneToMany(mappedBy = "cruiseShip", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Passenger> passengers;

    public CruiseShip() {}

    public CruiseShip(String name, int numOfDecks, int numOfPools, int numOfRooms) {
        this.name = name;
        this.numOfDecks = numOfDecks;
        this.numOfPools = numOfPools;
        this.numOfRooms = numOfRooms;
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

    public int getNumOfDecks() {
        return numOfDecks;
    }

    public void setNumOfDecks(int numOfDecks) {
        this.numOfDecks = numOfDecks;
    }

    public int getNumOfPools() {
        return numOfPools;
    }

    public void setNumOfPools(int numOfPools) {
        this.numOfPools = numOfPools;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CruiseShip that = (CruiseShip) o;
        return id == that.id ||
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
