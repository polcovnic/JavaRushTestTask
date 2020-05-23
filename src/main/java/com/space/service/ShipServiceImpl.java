package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipRepository shipRepository;


    @Override
    public List<Ship> getShipsList(String name, String planet, ShipType shipType, Long after, Long before,
                                   Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                   Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order,
                                   Optional<Integer> pageNumber, Optional<Integer> pageSize)
    {

        List<Ship> allShips = shipRepository.findAll();
        if (order != null) {
            allShips = allShips.stream()
                    .sorted((s1, s2) -> {
                        if (order.getFieldName().equals("id")) {
                            return (int) (s1.getId() - s2.getId());
                        }

                        if (order.getFieldName().equals("speed")) {
                            if (s1.getSpeed() < s2.getSpeed())
                                return -1;

                            if (s1.getSpeed() > s2.getSpeed())
                                return 1;

                            return 0;
                        }

                        if (order.getFieldName().equals("prodDate")) {
                            if (s1.getProdDate().getTime() < s2.getProdDate().getTime())
                                return -1;

                            if (s1.getProdDate().getTime() > s2.getProdDate().getTime())
                                return 1;

                            return 0;
                        }

                        if (s1.getRating() < s2.getRating())
                            return -1;

                        if (s1.getRating() > s2.getRating())
                            return 1;

                        return 0;
                    })
                    .filter(s -> name != null ? s.getName().contains(name) : true)
                    .filter(s -> planet != null ? s.getPlanet().contains(planet) : true)
                    .filter(s -> shipType != null ? s.getShipType().equals(shipType) : true)
                    .filter(s -> after != null ? s.getProdDate().getTime() >= after : true)
                    .filter(s -> before != null ? s.getProdDate().getTime() <= before : true)
                    .filter(s -> isUsed != null ? s.getUsed().equals(isUsed) : true)
                    .filter(s -> minSpeed != null ? s.getSpeed() >= minSpeed : true)
                    .filter(s -> maxSpeed != null ? s.getSpeed() <= maxSpeed : true)
                    .filter(s -> minCrewSize != null ? s.getCrewSize() >= minCrewSize : true)
                    .filter(s -> maxCrewSize != null ? s.getCrewSize() <= maxCrewSize : true)
                    .filter(s -> minRating != null ? s.getRating() >= minRating : true)
                    .filter(s -> maxRating != null ? s.getRating() <= maxRating : true)
                    .skip(pageSize.get() * pageNumber.get())
                    .limit(pageSize.get())
                    .collect(Collectors.toCollection(ArrayList::new));
        } else{
            allShips = allShips.stream()
                    .filter(s -> name != null ? s.getName().contains(name) : true)
                    .filter(s -> planet != null ? s.getPlanet().contains(planet) : true)
                    .filter(s -> shipType != null ? s.getShipType().equals(shipType) : true)
                    .filter(s -> after != null ? s.getProdDate().getTime() >= after : true)
                    .filter(s -> before != null ? s.getProdDate().getTime() <= before : true)
                    .filter(s -> isUsed != null ? s.getUsed().equals(isUsed) : true)
                    .filter(s -> minSpeed != null ? s.getSpeed() >= minSpeed : true)
                    .filter(s -> maxSpeed != null ? s.getSpeed() <= maxSpeed : true)
                    .filter(s -> minCrewSize != null ? s.getCrewSize() >= minCrewSize : true)
                    .filter(s -> maxCrewSize != null ? s.getCrewSize() <= maxCrewSize : true)
                    .filter(s -> minRating != null ? s.getRating() >= minRating : true)
                    .filter(s -> maxRating != null ? s.getRating() <= maxRating : true)
                    .skip(pageSize.get() * pageNumber.get())
                    .limit(pageSize.get())
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        //Page<Ship> page = new PageImpl<>(allShips, PageRequest.of(pageNumber.orElse(0), pageSize.orElse(3), Sort.by(order.getFieldName()).ascending()), allShips.size());

        return allShips;
    }


    @Override
    public Long getShipsCount(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {
        List<Ship> allShips = shipRepository.findAll();
        allShips = allShips.stream()
                .filter(s -> name != null ? s.getName().contains(name) : true)
                .filter(s -> planet != null ? s.getPlanet().contains(planet) : true)
                .filter(s -> shipType != null ? s.getShipType().equals(shipType) : true)
                .filter(s -> after != null ? s.getProdDate().getTime() >= after : true)
                .filter(s -> before != null ? s.getProdDate().getTime() <= before : true)
                .filter(s -> isUsed != null ? s.getUsed().equals(isUsed) : true)
                .filter(s -> minSpeed != null ? s.getSpeed() >= minSpeed : true)
                .filter(s -> maxSpeed != null ? s.getSpeed() <= maxSpeed : true)
                .filter(s -> minCrewSize != null ? s.getCrewSize() >= minCrewSize : true)
                .filter(s -> maxCrewSize != null ? s.getCrewSize() <= maxCrewSize : true)
                .filter(s -> minRating != null ? s.getRating() >= minRating : true)
                .filter(s -> maxRating != null ? s.getRating() <= maxRating : true)
                .collect(Collectors.toCollection(ArrayList::new));
        return (long)allShips.size();
    }



    @Override
    public Ship createShip(Ship ship) {
        try {
            ship.setRating(getRating(ship));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Ship createdShip = shipRepository.saveAndFlush(ship);
        return createdShip;
    }


    @Override
    public Ship getShip(Long id) {
        return shipRepository.getShip(id);
    }


    @Override
    public Ship updateShip(Long id, Ship ship) {
        Ship foundShip = getShip(id);
        if (foundShip == null)
            return null;

        if (ship.getName() != null)
            foundShip.setName(ship.getName());

        if (ship.getPlanet() != null)
            foundShip.setPlanet(ship.getPlanet());

        if (ship.getShipType() != null)
            foundShip.setShipType(ship.getShipType());

        if (ship.getProdDate() != null)
            foundShip.setProdDate(ship.getProdDate());

        if (ship.getUsed() != null)
            foundShip.setUsed(ship.getUsed());

        if (ship.getSpeed() != null)
            foundShip.setSpeed(ship.getSpeed());

        if (ship.getCrewSize() != null)
            foundShip.setCrewSize(ship.getCrewSize());

        try {
            foundShip.setRating(getRating(foundShip));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return shipRepository.saveAndFlush(foundShip);
    }


    @Override
    public Boolean deleteShip(Long id) {
        Ship ship = shipRepository.getShip(id);
        if (ship == null)
            return false;
        shipRepository.delete(ship);
        return true;
    }

    private static Double getRating(Ship ship) throws ParseException {
        Double k = ship.getUsed() ? 0.5 : 1;

        Calendar shipProd = Calendar.getInstance();
        shipProd.setTime(ship.getProdDate());
        int shipProdYear = shipProd.get(Calendar.YEAR);

        Double rating = 80 * ship.getSpeed() * k / (3019 - shipProdYear + 1);
        return new BigDecimal(rating).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}