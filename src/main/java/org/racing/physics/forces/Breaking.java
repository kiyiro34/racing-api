package org.racing.physics.forces;

import org.racing.physics.geometry.Vector;
import org.racing.entities.vehicles.Drone;

import java.time.Duration;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.racing.utilities.ForceConstants.BREAKING_MAX;

public class Breaking extends Force{
    private final double breakingRatio;

    public Breaking(Drone drone){
        super(drone);
        this.breakingRatio = 0.0;
    }

    public Breaking(Drone drone, Vector vector, double breakingRatio){
        super(drone,vector);
        this.breakingRatio = breakingRatio;
    }

    @Override
    public Breaking update(Drone drone, Duration duration) {
        var breakingX = drone.getMass()*breakingRatio*BREAKING_MAX* sin(drone.getWheelsHeading());
        var breakingY = drone.getMass()*breakingRatio*BREAKING_MAX* cos(drone.getWheelsHeading());
        return new Breaking(drone,new Vector(breakingX,breakingY),breakingRatio);
    }
}
