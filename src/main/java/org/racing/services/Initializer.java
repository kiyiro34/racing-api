package org.racing.services;

import org.racing.entities.circuit.Circuit;
import org.racing.entities.circuit.Race;
import org.racing.entities.vehicles.Drone;
import org.racing.entities.vehicles.Motor;
import org.racing.physics.geometry.Point;

import java.util.ArrayList;
import java.util.List;

import static org.racing.utilities.Services.SETTER;

public class Initializer {
    public static Race RACE(){
        List<Point> points = List.of(
                new Point(0, 0),
                new Point(20, 50),
                new Point(50, 100),
                new Point(100, 100),
                new Point(200, 100),
                new Point(300, 50),
                new Point(300, 20),
                new Point(280, -100),
                new Point(150, -80),
                new Point(50, -60)
        );
        Circuit circuit = SETTER.of(points);
        List<Drone> droneList = new ArrayList<>();
        droneList.add(new Drone("Lamborghini", new Motor(0, 10), 1.5));
        return new Race(droneList, circuit);
    }
}
