package org.racing.circuit;

import org.racing.geometry.CoordinateSystem;
import org.racing.geometry.Vector;
import org.racing.vehicles.Car;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Math.*;

public record Circuit(List<Line> lines, CoordinateSystem system) {

    public void initCar(List<Car> cars){
        cars.forEach(car->car.setPosition(system.origin()));
        cars.forEach(car->car.updateNextPoint(lines.getFirst().segment().end()));
        cars.forEach(car->car.setLastPoint(lines.getFirst().segment().start()));

    }
    public void initCar(Car car){
        car.setPosition(system.origin());
        car.updateNextPoint(lines.getFirst().segment().end());
        car.setLastPoint(lines.getFirst().segment().start());
        car.start();
    }

    public void updateNextPoint(List<Car> cars){
        cars.forEach(this::updatePoint);
    }

    private void updatePoint(Car car) {
        var bestPoint = lines.stream()
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
