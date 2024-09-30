package org.racing.forces;

import lombok.Getter;
import lombok.Setter;
import org.racing.geometry.Point;
import org.racing.geometry.Vector;
import org.racing.vehicles.Car;

import java.time.Duration;
import java.time.Instant;

@Setter
@Getter
public abstract class Force {
    private Vector vector;
    private Point origin;

    public Force(Point origin){
        this.origin = origin;
        this.vector = Vector.init();
    }

    public Force(Car car){
        this.vector = Vector.init();
        this.origin = car.getPosition();
    }

    public Force(Car car,Vector vector){
        this.vector = vector;
        this.origin = car.getPosition();
    }

    public abstract Force update(Car car, Duration duration);

}
