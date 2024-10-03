package org.racing.controllers;

import org.racing.geometry.Point;
import org.racing.services.RaceMaintainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
