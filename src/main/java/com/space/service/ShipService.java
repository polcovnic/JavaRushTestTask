package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.List;
import java.util.Optional;

public interface ShipService {
    List<Ship> getShipsList(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpead,
                            Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order,
                            Optional<Integer> pageNumber, Optional<Integer> pageSize);
    Long getShipsCount(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpead,
                       Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating);
    Ship createShip(Ship ship);
    Ship getShip(Long id);
    Ship updateShip(Long id, Ship ship);
    Boolean deleteShip(Long id);
}