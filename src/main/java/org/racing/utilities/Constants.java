package org.racing.utilities;

import org.racing.circuit.Circuit;
import org.racing.circuit.Line;
import org.racing.circuit.Race;
import org.racing.geometry.CoordinateSystem;
import org.racing.geometry.Point;
import org.racing.geometry.Segment;
import org.racing.geometry.Vector;
import org.racing.vehicles.Car;
import org.racing.vehicles.Motor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final double R_ROUE = 0.620/2;
    public static final double R_TRANSMISSION = 1.0;
    public static final Duration STARTING_DURATION = Duration.ofSeconds(1);
    public static final double MILLI_TO_SECONDS = 1E-3;
    public static final double G = 9.81;

    public static final Race RACE = RACE();

    public static Race RACE(){
        CoordinateSystem system = new CoordinateSystem(new Point(0,0), new Vector(1,0), new Vector(0,1));
        Line line = new Line(new Segment(new Point(0,0), new Point(20,50)), 50);
        Line line2 = new Line(new Segment(new Point(50,100), new Point(100,100)), 50);
        Line line3 = new Line(new Segment(new Point(200,100), new Point(300,50)), 50);


        Circuit circuit = new Circuit(List.of(line,line2,line3), system);

        List<Car> carList = new ArrayList<>();
        carList.add(new Car("Mercedes", new Motor(441299, 10000), 500));
        return new Race(carList, circuit);
    }
}
