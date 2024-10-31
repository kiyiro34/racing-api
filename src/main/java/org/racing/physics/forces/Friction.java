package org.racing.physics.forces;

import org.racing.physics.geometry.Point;
import org.racing.physics.geometry.Vector;
import org.racing.entities.vehicles.Car;

import java.time.Duration;

import static org.racing.utilities.ForceConstants.FRICTION_COEFFICIENT;
import static org.racing.utilities.ForceConstants.G;

public class Friction extends Force{
    public Friction(Point origin) {
        super(origin);
    }

    public Friction(Car car){
        super(car);
    }

    public Friction(Car car, Vector vector){
        super(car,vector);
    }

    @Override
    public Friction update(Car car, Duration duration) {
        var frictionX = -FRICTION_COEFFICIENT*car.getMass()*G*car.getSpeed().x() /*Replace with heading*/;
        var frictionY = -FRICTION_COEFFICIENT*car.getMass()*G*car.getSpeed().y() /*Replace with heading*/;
        return new Friction(car,new Vector(frictionX,frictionY));
    }
}
