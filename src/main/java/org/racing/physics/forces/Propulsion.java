package org.racing.physics.forces;

import lombok.ToString;
import org.racing.physics.geometry.Point;
import org.racing.physics.geometry.Vector;
import org.racing.entities.vehicles.Car;

import java.time.Duration;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.racing.utilities.Constants.R_ROUE;

@ToString
public class Propulsion extends Force{

    private double powerRatio;

    public Propulsion(Point origin) {
        super(origin);
        this.powerRatio = 0.0;
    }

    public Propulsion(Car car){
        super(car);
        this.powerRatio = 0.0;
    }

    public Propulsion(Car car,Vector vector,double powerRatio){
        super(car,vector);
        this.powerRatio = powerRatio;
    }

    @Override
    public Propulsion update(Car car, Duration duration) {
        if (car.getSpeed().norm()==0) return new Propulsion(car);
        var power = car.getMotor().couple()*powerRatio/R_ROUE;
        var powerX = power * sin(car.getWheelsHeading()) /*Replace with heading*/;
        var powerY = power * cos(car.getWheelsHeading());
        return new Propulsion(car,new Vector(powerX,powerY),powerRatio);
    }
}
