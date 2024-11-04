package org.racing.physics.forces;

import lombok.ToString;
import org.racing.physics.geometry.Vector;
import org.racing.entities.vehicles.Drone;

import java.time.Duration;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.racing.utilities.Constants.R_ROUE;

@ToString
public class Propulsion extends Force{
    private final double powerRatio;

    public Propulsion(Drone drone){
        super(drone);
        this.powerRatio = 0.0;
    }

    public Propulsion(Drone drone, Vector vector, double powerRatio){
        super(drone,vector);
        this.powerRatio = powerRatio;
    }

    @Override
    public Propulsion update(Drone drone, Duration duration) {
        if (drone.getSpeed().norm()==0) return new Propulsion(drone);
        var power = drone.getMotor().couple()*powerRatio/R_ROUE;
        var powerX = power * sin(drone.getWheelsHeading()) /*Replace with heading*/;
        var powerY = power * cos(drone.getWheelsHeading());
        return new Propulsion(drone,new Vector(powerX,powerY),powerRatio);
    }
}
