package org.racing.physics.forces;

import org.racing.physics.geometry.Vector;
import org.racing.entities.vehicles.Drone;

import java.time.Duration;

import static org.racing.utilities.ForceConstants.FRICTION_COEFFICIENT;
import static org.racing.utilities.ForceConstants.G;

public class Friction extends Force{

    public Friction(Drone drone){
        super(drone);
    }

    public Friction(Drone drone, Vector vector){
        super(drone,vector);
    }

    @Override
    public Friction update(Drone drone, Duration duration) {
        var frictionX = -FRICTION_COEFFICIENT* drone.getMass()*G* drone.getSpeed().x() /*Replace with heading*/;
        var frictionY = -FRICTION_COEFFICIENT* drone.getMass()*G* drone.getSpeed().y() /*Replace with heading*/;
        return new Friction(drone,new Vector(frictionX,frictionY));
    }
}
