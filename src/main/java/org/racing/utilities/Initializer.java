package org.racing.utilities;

import org.racing.entities.circuit.Circuit;
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

public class Initializer {
    public static Race RACE(){
        CoordinateSystem system = new CoordinateSystem(new Point(0,0), new Vector(1,0), new Vector(0,1));
        Line line = new Line(new Segment(new Point(0,0), new Point(20,50)), 50);
        Line line2 = new Line(new Segment(new Point(50,100), new Point(100,100)), 50);
        Line line3 = new Line(new Segment(new Point(200,100), new Point(300,50)), 50);
        Line line4 = new Line(new Segment(new Point(300,20), new Point(280,-100)), 50);
        Line line5 = new Line(new Segment(new Point(150,-80), new Point(50,-60)), 50);
        Circuit circuit = new Circuit(List.of(line,line2,line3,line4,line5), system);

        List<Car> carList = new ArrayList<>();
        carList.add(new Car("Loïc", new Motor(0, 10), 1.5));
        return new Race(carList, circuit);
    }
}
