package org.racing.services;

import org.racing.entities.circuit.Line;
import org.racing.entities.circuit.Circuit;
import org.racing.entities.vehicles.Drone;
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

    public void initCar(Circuit circuit, List<Drone> drones){
        drones.forEach(car->car.setPosition(circuit.system().origin()));
        drones.forEach(car->car.updateNextPoint(circuit.lines().getFirst().segment().end()));
        drones.forEach(car->car.setLastPoint(circuit.lines().getFirst().segment().start()));

    }
    public void initOneCar(Circuit circuit, Drone drone){
        drone.setPosition(circuit.system().origin());
        drone.updateNextPoint(circuit.lines().getFirst().segment().end());
        drone.setLastPoint(circuit.lines().getFirst().segment().start());
        drone.getPointList().add(drone.getLastPoint());
        drone.start();
    }

    public void updateNextPoint(Circuit circuit,List<Drone> drones){
        drones.forEach(car -> updatePoint(circuit,car));
    }

    private void updatePoint(Circuit circuit, Drone drone) {
        var bestPoint = circuit.lines().stream()
                .flatMap(line -> Stream.of(line.segment().start(), line.segment().end()))
                .filter(point->!point.equals(drone.getLastPoint()))
                .map(point -> {
                    var vector = Vector.of(drone.getPosition(), point);
                    double angleCost = cost(drone.getSpeed().normalize().heading(vector), drone.getPosition().distance(point));
                    return new AbstractMap.SimpleEntry<>(point, angleCost);
                })
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .get()
                .getKey();
        drone.updateNextPoint(bestPoint);
    }
    private double cost(double angle, double distance){
        return abs(pow(Math.toDegrees(angle),2)+(distance));
    }
}
