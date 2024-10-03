package org.racing.forces;

import org.racing.geometry.Point;
import org.racing.geometry.Vector;
import org.racing.vehicles.Car;

import java.time.Duration;

import static org.racing.utilities.Constants.G;
import static org.racing.utilities.Constants.R_ROUE;

public class Friction extends Force{

    public static final double COEFF = 0.05;

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
        var frictionX = -COEFF*car.getMass()*G*car.getSpeed().x() /*Replace with heading*/;
        var frictionY = -COEFF*car.getMass()*G*car.getSpeed().y() /*Replace with heading*/;
        return new Friction(car,new Vector(frictionX,frictionY));
    }
}
