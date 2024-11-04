package org.racing.controllers;

import org.racing.entities.vehicles.Drone;
import org.racing.entities.vehicles.Motor;
import org.racing.models.DroneExternal;
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

    @PostMapping("/simulation/reset")
    public void resetSimulation() {
        raceMaintainer.reset();
    }

    @GetMapping("/circuit/points")
    public List<Point> circuit() {
        return raceMaintainer.getCircuitPoints();
    }

    @PostMapping("/drone/add")
    public void addCar(@RequestBody DroneExternal droneData) {
        Drone newDrone = new Drone(droneData.brand, new Motor(0, droneData.power), droneData.mass);
        raceMaintainer.addCar(newDrone);
    }
}
