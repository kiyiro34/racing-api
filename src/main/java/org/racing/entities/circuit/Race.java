package org.racing.entities.circuit;

import org.racing.entities.vehicles.Drone;
import org.racing.physics.geometry.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static org.racing.utilities.Services.SETTER;

public record Race(List<Drone> drones, Circuit circuit) {
    public Race(List<Drone> drones, Circuit circuit) {
        this.drones = new ArrayList<>(drones);
        this.circuit = circuit;
        SETTER.initCar(this.circuit, this.drones);
    }

    public void updatePoint() {
        SETTER.updateNextPoint(circuit, drones);
    }


    public void addCar(Drone drone) {
        SETTER.initOneCar(circuit, drone);
        this.drones.add(drone);
    }

    public List<Point> points() {
        return circuit.lines().stream().flatMap(line -> Stream.of(line.segment().start(), line.segment().end())).toList();
    }

}
