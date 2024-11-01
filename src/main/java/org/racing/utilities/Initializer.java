package org.racing.utilities;

import org.racing.models.Circuit;
import org.racing.entities.circuit.Line;
import org.racing.entities.circuit.Race;
import org.racing.entities.vehicles.Car;
import org.racing.entities.vehicles.Motor;
import org.racing.physics.geometry.CoordinateSystem;
import org.racing.physics.geometry.Point;
import org.racing.physics.geometry.Segment;
import org.racing.physics.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

import static org.racing.utilities.Services.SETTER;

public class Initializer {
    public static Race RACE(){
        CoordinateSystem system = new CoordinateSystem(new Point(0,0), new Vector(1,0), new Vector(0,1));
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
        List<Car> carList = new ArrayList<>();
        carList.add(new Car("Loïc", new Motor(0, 10), 1.5));
        return new Race(carList, circuit);
    }
}
