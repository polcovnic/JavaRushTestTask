package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShipRepository extends JpaRepository<Ship, Long> {

    @Query("select b from Ship b where b.id = :id")
    Ship getShip(@Param("id") Long id);


}