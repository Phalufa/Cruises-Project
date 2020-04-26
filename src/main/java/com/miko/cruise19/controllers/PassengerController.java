package com.miko.cruise19.controllers;

import com.miko.cruise19.models.CruiseShip;
import com.miko.cruise19.models.Passenger;
import com.miko.cruise19.models.Status;
import com.miko.cruise19.repositories.CruiseShipRepository;
import com.miko.cruise19.repositories.PassengerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/passengers")
@CrossOrigin(value = {"http://localhost:4200"})
public class PassengerController {

    private final PassengerRepository passengerRepo;
    private final CruiseShipRepository shipRepo;

    public PassengerController(PassengerRepository passengerRepo, CruiseShipRepository shipRepo) {
        this.passengerRepo = passengerRepo;
        this.shipRepo = shipRepo;
    }

    @PostMapping
    public ResponseEntity<?> createPassenger(@RequestBody Passenger passenger) {
        if (!shipRepo.existsById(passenger.getCruiseShip().getId()))
            return ResponseEntity.badRequest()
                    .body("Invalid cruise ship");

        if (passenger.getStatus() == Status.PENDING) {
            passengerRepo.save(passenger);
            return ResponseEntity.ok().body(passenger.getName() + " has been updated");
        }

        if (isValidRoom(passenger) && isSpaceLeft(passenger)) {
            passengerRepo.save(passenger);
            return ResponseEntity.ok().body(passenger.getName() + " has been added");
        }
        return ResponseEntity.badRequest()
                .body("Invalid room number");
    }

    private boolean isSpaceLeft(Passenger passenger) {
        return shipRepo.findById(passenger.getCruiseShip().getId())
                .get()
                .getPassengers()
                .stream()
                .noneMatch(p -> p.getRoomNumber() == passenger.getRoomNumber() && p.getId() != passenger.getId());
    }

    private boolean isValidRoom(Passenger passenger) {
        return passenger.getCruiseShip().getNumOfRooms() >= passenger.getRoomNumber() &&
                     passenger.getRoomNumber() >= 1;
    }

    @PutMapping
    public ResponseEntity<?> updatePassenger(@RequestBody Passenger passenger) {
        if (!passengerRepo.existsById(passenger.getId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Passenger " + passenger.getName() + " not found");

        if (passenger.getStatus() == Status.PENDING) {
//            passenger.setRoomNumber(0);
//            passenger.setCruiseShip(null);
            passengerRepo.save(passenger);
            return ResponseEntity.ok().body(passenger.getName() + " has been updated");
        }

        if (isValidRoom(passenger) && isSpaceLeft(passenger)) {
            shipRepo.findAll()
                    .stream()
                    .map(CruiseShip::getPassengers)
                    .forEach(list -> list.remove(passenger));
            CruiseShip cs = passenger.getCruiseShip();
            shipRepo.save(cs);
            Passenger p = passengerRepo.save(passenger);
            return ResponseEntity.ok(passenger);
        }
        return ResponseEntity.badRequest()
                .body("Invalid room number");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePassengerById(@PathVariable long id) {
            passengerRepo.deleteById(id);
            return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPassengerById(@PathVariable long id) {
        return ResponseEntity.of(passengerRepo.findById(id));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> getPassengerByName(@PathVariable String name) {
        return ResponseEntity.of(passengerRepo.findByName(name));
    }

    @GetMapping
    public ResponseEntity<?> getPassengers() {
        List<Passenger> passengers = passengerRepo.findAll();
        if (passengers.size() > 0)
            return ResponseEntity.ok(passengers);

        return ResponseEntity.badRequest().body("There are no passengers");
    }
}
