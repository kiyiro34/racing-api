package org.racing.entities.circuit;

import lombok.Getter;
import org.racing.entities.vehicles.Car;
import org.racing.models.Circuit;
import org.racing.physics.geometry.Point;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static org.racing.utilities.Services.SETTER;

@Getter
public record Race(List<Car> cars, Circuit circuit) {
    public Race(List<Car> cars, Circuit circuit) {
        this.cars = new ArrayList<>(cars);
        this.circuit = circuit;
        SETTER.initCar(this.circuit, this.cars);
    }

    public void updatePoint() {
        SETTER.updateNextPoint(circuit, cars);
    }

    public void checkCars(Duration duration) {
        cars.forEach(car -> {
            if (points().equals(car.getPointList())) {
                car.stop(duration);
            }
        });
    }

    public void addCar(Car car) {
        SETTER.initOneCar(circuit, car);
        this.cars.add(car);
    }

    private List<Point> points() {
        return circuit.lines().stream().flatMap(line -> Stream.of(line.segment().start(), line.segment().end())).toList();
    }

}
