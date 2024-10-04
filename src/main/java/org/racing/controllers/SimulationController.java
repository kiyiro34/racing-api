package org.racing.controllers;

import org.racing.model.ModelCar;
import org.racing.geometry.Point;
import org.racing.services.RaceMaintainer;
import org.racing.vehicles.Car;
import org.racing.vehicles.Motor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Autorise toutes les origines
public class SimulationController {

    private final RaceMaintainer raceMaintainer;

    @Autowired
    public SimulationController(RaceMaintainer raceMaintainer) {
        this.raceMaintainer = raceMaintainer;
    }

    @PostMapping("/stopSimulation")
    public void stopSimulation() {
        raceMaintainer.stopSimulation();
    }

    @GetMapping("/circuitPoint")
    public List<Point> circuit() {
        return raceMaintainer.getCircuitPoints();
    }

    @PostMapping("/addCar")
    public String addCar(@RequestBody ModelCar carData){
        Car newCar = new Car(carData.brand,new Motor(0,carData.power),carData.mass);
        raceMaintainer.addCar(newCar);
        return "Car added";
    }
}
