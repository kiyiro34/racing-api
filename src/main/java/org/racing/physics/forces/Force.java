package org.racing.physics.forces;

import lombok.Getter;
import lombok.Setter;
import org.racing.physics.geometry.Point;
import org.racing.physics.geometry.Vector;
import org.racing.entities.vehicles.Car;

import java.time.Duration;

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
