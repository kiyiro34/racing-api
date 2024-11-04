package org.racing.physics.forces;

import lombok.Getter;
import lombok.Setter;
import org.racing.physics.geometry.Point;
import org.racing.physics.geometry.Vector;
import org.racing.entities.vehicles.Drone;

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

    public Force(Drone drone){
        this.vector = Vector.init();
        this.origin = drone.getPosition();
    }

    public Force(Drone drone, Vector vector){
        this.vector = vector;
        this.origin = drone.getPosition();
    }

    public abstract Force update(Drone drone, Duration duration);

}
