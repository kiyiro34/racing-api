package org.racing.forces;

import org.racing.geometry.Point;
import org.racing.geometry.Vector;
import org.racing.vehicles.Car;

import java.time.Duration;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.racing.utilities.Constants.R_ROUE;

public class Breaking extends Force{

    private double breakingRatio;
    private static final double BREAKING_MAX = -40;

    public Breaking(Point origin) {
        super(origin);
        this.breakingRatio = 0.0;
    }

    public Breaking(Car car){
        super(car);
        this.breakingRatio = 0.0;
    }

    public Breaking(Car car, Vector vector, double breakingRatio){
        super(car,vector);
        this.breakingRatio = breakingRatio;
    }

    @Override
    public Breaking update(Car car, Duration duration) {
        var breakingX = car.getMass()*breakingRatio*BREAKING_MAX* sin(car.getWheelsHeading());
        var breakingY = car.getMass()*breakingRatio*BREAKING_MAX* cos(car.getWheelsHeading());
        return new Breaking(car,new Vector(breakingX,breakingY),breakingRatio);
    }
}
