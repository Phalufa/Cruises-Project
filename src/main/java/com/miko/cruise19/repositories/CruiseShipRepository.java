package com.miko.cruise19.repositories;

import com.miko.cruise19.models.CruiseShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CruiseShipRepository extends JpaRepository<CruiseShip, Long> {

    Optional<CruiseShip> findByName(String name);
}
