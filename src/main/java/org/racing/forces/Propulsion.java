package org.racing.forces;

import lombok.ToString;
import org.racing.geometry.Point;
import org.racing.geometry.Vector;
import org.racing.vehicles.Car;

import java.time.Duration;

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
        var powerX = car.getMotor().couple()*powerRatio/R_ROUE* 1 /*Replace with heading*/;
//        var powerY = car.getSpeed().y()*powerRatio*car.getMotor().power()/car.getSpeed().norm();
        var powerY = 0;
        return new Propulsion(car,new Vector(powerX,powerY),powerRatio);
    }
}
