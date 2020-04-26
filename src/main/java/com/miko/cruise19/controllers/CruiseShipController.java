package com.miko.cruise19.controllers;

import com.miko.cruise19.models.CruiseShip;
import com.miko.cruise19.models.Status;
import com.miko.cruise19.repositories.CruiseShipRepository;
import com.miko.cruise19.repositories.PassengerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import java.util.List;

@RestController
@RequestMapping("/ships")
@CrossOrigin(value = {"http://localhost:4200"})
public class CruiseShipController {

    private final CruiseShipRepository shipRepo;
    private final PassengerRepository passengerRepo;

    public CruiseShipController(CruiseShipRepository shipRepo, PassengerRepository passengerRepo) {
        this.shipRepo = shipRepo;
        this.passengerRepo = passengerRepo;
    }

    @PostMapping
    public ResponseEntity<?> createShip(@RequestBody CruiseShip cruiseShip) {
        if (!shipRepo.findAll().contains(cruiseShip)) {
            CruiseShip addedShip = shipRepo.save(cruiseShip);
            return ResponseEntity.ok(addedShip);
        }
        return ResponseEntity.badRequest().body("Another cruise ship with the same name already exists");
    }

    @GetMapping
    public ResponseEntity<?> getFleet() {
        List<CruiseShip> fleet = shipRepo.findAll();
        if (fleet.size() > 0)
            return ResponseEntity.ok(fleet);

        return ResponseEntity.badRequest().body("There are no cruise ships");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShipById(@PathVariable long id) {
        return ResponseEntity.of(shipRepo.findById(id));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> getShipByName(@PathVariable String name) {
        return ResponseEntity.of(shipRepo.findByName(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShipById(@PathVariable long id) {
        shipRepo.findById(id).ifPresent(ship -> {
            ship.getPassengers().forEach(passenger -> {
                        passenger.setStatus(Status.PENDING);
                        passenger.setCruiseShip(null);
                        passenger.setRoomNumber(0);
                        passengerRepo.save(passenger);
                    });
            ship.getPassengers().clear();
            shipRepo.save(ship);
        });
        shipRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> updateShip(@RequestBody CruiseShip cruiseShip) {
        if (!shipRepo.existsById(cruiseShip.getId()))
            return ResponseEntity.badRequest().body("Ship doesn't exists");
        shipRepo.findAll().forEach(s -> {
            if (s.getName().equals(cruiseShip.getName()) && s.getId() != cruiseShip.getId()) {
                try {
                    throw new NamingException();
                } catch (NamingException e) {
                    ResponseEntity.badRequest().body("Another ship with the same name already exists");
                }
            } else {

                shipRepo.save(cruiseShip);
            }
        });
        return ResponseEntity.ok(cruiseShip.getName() + " has been updated");
    }
}
