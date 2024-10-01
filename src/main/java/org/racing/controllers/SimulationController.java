package org.racing.controllers;

import org.racing.services.RaceMaintainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimulationController {

    private final RaceMaintainer raceMaintainer;

    @Autowired
    public SimulationController(RaceMaintainer raceMaintainer) {
        this.raceMaintainer = raceMaintainer;
    }

    @PostMapping("/stopSimulation")
    public void stopSimulation() {
        raceMaintainer.stopSimulation(); // Appelle la méthode pour arrêter la simulation
    }
}
