package org.racing.circuit;

import lombok.Getter;
import org.racing.vehicles.Car;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Race {
    private List<Line> parts;
    private List<Car> cars;
    private Instant time;

    public Race(List<Car> cars){
        this.cars=new ArrayList<>(cars);
    }
}
