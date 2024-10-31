package org.racing.entities.circuit;

import lombok.Getter;
import org.racing.entities.vehicles.Car;
import org.racing.physics.geometry.Point;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.racing.utilities.Constants.RACE;

@Getter
public class Race {
    private Circuit circuit;
    private List<Car> cars;
    private Instant time;

    public Race(List<Car> cars, Circuit circuit){
        this.cars=new ArrayList<>(cars);
        this.circuit = circuit;
        this.circuit.initCar(cars);
    }

    public void updatePoint(){
        this.circuit.updateNextPoint(cars);
    }

    public void checkCars(Duration duration){
        cars.forEach(car ->{
            if (points().equals(car.getPointList())){
                car.stop(duration);
            }
        });
    }

    public void addCar(Car car){
        this.circuit.initOneCar(car);
        this.cars.add(car);
    }

    private List<Point> points(){
        return circuit.lines().stream().flatMap(line -> Stream.of(line.segment().start(), line.segment().end())).toList();
    }

}
