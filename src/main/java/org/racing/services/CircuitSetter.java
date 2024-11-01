package org.racing.services;

import org.racing.entities.circuit.Line;
import org.racing.entities.circuit.Circuit;
import org.racing.entities.vehicles.Car;
import org.racing.physics.geometry.CoordinateSystem;
import org.racing.physics.geometry.Point;
import org.racing.physics.geometry.Segment;
import org.racing.physics.geometry.Vector;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class CircuitSetter {

    public Circuit of(List<Point> points){
        var system = new CoordinateSystem(new Point(0,0), new Vector(1,0), new Vector(0,1));
        var lines = IntStream.range(0, points.size() - 1)
                .mapToObj(i -> new Segment(points.get(i), points.get(i + 1)))
                .map(seg->new Line(seg,50))
                .toList();
        return new Circuit(lines,system);
    }

    public void initCar(Circuit circuit, List<Car> cars){
        cars.forEach(car->car.setPosition(circuit.system().origin()));
        cars.forEach(car->car.updateNextPoint(circuit.lines().getFirst().segment().end()));
        cars.forEach(car->car.setLastPoint(circuit.lines().getFirst().segment().start()));

    }
    public void initOneCar(Circuit circuit,Car car){
        car.setPosition(circuit.system().origin());
        car.updateNextPoint(circuit.lines().getFirst().segment().end());
        car.setLastPoint(circuit.lines().getFirst().segment().start());
        car.getPointList().add(car.getLastPoint());
        car.start();
    }

    public void updateNextPoint(Circuit circuit,List<Car> cars){
        cars.forEach(car -> updatePoint(circuit,car));
    }

    private void updatePoint(Circuit circuit,Car car) {
        var bestPoint = circuit.lines().stream()
                .flatMap(line -> Stream.of(line.segment().start(), line.segment().end()))
                .filter(point->!point.equals(car.getLastPoint()))
                .map(point -> {
                    var vector = Vector.of(car.getPosition(), point);
                    double angleCost = cost(car.getSpeed().normalize().heading(vector), car.getPosition().distance(point));
                    return new AbstractMap.SimpleEntry<>(point, angleCost);
                })
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .get()
                .getKey();
        car.updateNextPoint(bestPoint);
    }
    private double cost(double angle, double distance){
        return abs(pow(Math.toDegrees(angle),2)+(distance));
    }
}
