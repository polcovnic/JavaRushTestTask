package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ShipController {
    private ShipService shipService;

    @Autowired
    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping(value = "rest/ships")
    public ResponseEntity<List<Ship>> getShipsList(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "planet", required = false) String planet,
                                                   @RequestParam(value = "shipType", required = false) ShipType shipType, @RequestParam(value = "after", required = false) Long after,
                                                   @RequestParam(value = "before", required = false) Long before, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                                   @RequestParam(value = "minSpeed", required = false) Double minSpeed, @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                                   @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize, @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                                   @RequestParam(value = "minRating", required = false) Double minRating, @RequestParam(value = "maxRating", required = false) Double maxRating,
                                                   @RequestParam(value = "order", required = false) ShipOrder order, @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        if (pageNumber == null)
            pageNumber = 0;

        if (pageSize == null)
            pageSize = 3;

        List<Ship> shipList = shipService.getShipsList(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating,
                maxRating, order, Optional.of(pageNumber), Optional.of(pageSize));

        return shipList != null && !shipList.isEmpty()
                ? new ResponseEntity<>(shipList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "rest/ships/count")
    public ResponseEntity<Long> getShipsCount(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "planet", required = false) String planet,
                                              @RequestParam(value = "shipType", required = false) ShipType shipType, @RequestParam(value = "after", required = false) Long after,
                                              @RequestParam(value = "before", required = false) Long before, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                              @RequestParam(value = "minSpeed", required = false) Double minSpeed, @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                              @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize, @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                              @RequestParam(value = "minRating", required = false) Double minRating, @RequestParam(value = "maxRating", required = false) Double maxRating
    ) {

        Long shipsCount = shipService.getShipsCount(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating,
                maxRating);


        return new ResponseEntity<>(shipsCount, HttpStatus.OK);
    }

    @PostMapping(value = "rest/ships")
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) throws ParseException {
        if (ship.getName() == null || ship.getPlanet() == null || ship.getShipType() == null || ship.getProdDate() == null || ship.getSpeed() == null
                || ship.getCrewSize() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (ship.getName().isEmpty() || ship.getPlanet().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (ship.getName().length() > 50 || ship.getPlanet().length() > 50)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (ship.getUsed() == null)
            ship.setUsed(false);

        Calendar startRangeCalendar = new GregorianCalendar(2800, Calendar.JANUARY, 1);

        Date d = new SimpleDateFormat("MM/dd/yyyy").parse("01/12/3019");
        Calendar finishRangeCalendar = Calendar.getInstance();
        finishRangeCalendar.setTime(d);
        finishRangeCalendar.set(Calendar.DATE, finishRangeCalendar.getActualMaximum(Calendar.DATE));
        finishRangeCalendar.set(Calendar.HOUR, 23);
        finishRangeCalendar.set(Calendar.MINUTE, 59);
        finishRangeCalendar.set(Calendar.SECOND, 59);

        if (ship.getProdDate().before(startRangeCalendar.getTime()) || ship.getProdDate().after(finishRangeCalendar.getTime()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship newShip = shipService.createShip(ship);
        return new ResponseEntity<>(newShip, HttpStatus.OK);
    }

    @GetMapping(value = "rest/ships/{id}")
    public ResponseEntity<Ship> getShip(@PathVariable Long id) {
        if (id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shipService.getShip(id);

        return ship != null ? new ResponseEntity<>(ship, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "rest/ships/{id}")
    public ResponseEntity<?> updateShip(@PathVariable Long id, @RequestBody Ship ship) throws ParseException {
        if (id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (ship.getName() != null) {
            if (ship.getName().isEmpty() || ship.getName().length() > 50)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (ship.getPlanet() != null){
            if (ship.getPlanet().isEmpty() || ship.getPlanet().length() > 50)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (ship.getProdDate() != null){
            Calendar startRangeCalendar = new GregorianCalendar(2800, Calendar.JANUARY, 1);

            Date d = new SimpleDateFormat("MM/dd/yyyy").parse("01/12/3019");
            Calendar finishRangeCalendar = Calendar.getInstance();
            finishRangeCalendar.setTime(d);
            finishRangeCalendar.set(Calendar.DATE, finishRangeCalendar.getActualMaximum(Calendar.DATE));
            finishRangeCalendar.set(Calendar.HOUR, 23);
            finishRangeCalendar.set(Calendar.MINUTE, 59);
            finishRangeCalendar.set(Calendar.SECOND, 59);

            if (ship.getProdDate().before(startRangeCalendar.getTime()) || ship.getProdDate().after(finishRangeCalendar.getTime()))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (ship.getSpeed() != null) {
            if (ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (ship.getCrewSize() != null) {
            if (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Ship updatedShip = shipService.updateShip(id, ship);
        return updatedShip != null ? new ResponseEntity<>(updatedShip, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "rest/ships/{id}")
    public ResponseEntity<?> deleteShip(@PathVariable Long id) {
        if (id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Boolean isDeleted = shipService.deleteShip(id);

        return isDeleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}