package org.racing.controllers;

import org.racing.entities.vehicles.Car;
import org.racing.entities.vehicles.Motor;
import org.racing.models.CarExternal;
import org.racing.physics.geometry.Point;
import org.racing.services.RaceMaintainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//Allows all the origins for the moment
public class SimulationController {

    private final RaceMaintainer raceMaintainer;

    @Autowired
    public SimulationController(RaceMaintainer raceMaintainer) {
        this.raceMaintainer = raceMaintainer;
    }

    @PostMapping("/simulation/stop")
    public void stopSimulation() {
        raceMaintainer.stopSimulation();
    }

    @GetMapping("/circuit/points")
    public List<Point> circuit() {
        return raceMaintainer.getCircuitPoints();
    }

    @PostMapping("/car/add")
    public String addCar(@RequestBody CarExternal carData) {
        Car newCar = new Car(carData.brand, new Motor(0, carData.power), carData.mass);
        raceMaintainer.addCar(newCar);
        return "Car added";
    }
}
