package org.racing.vehicles;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.racing.forces.Force;
import org.racing.forces.Propulsion;
import org.racing.geometry.Point;
import org.racing.geometry.Vector;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.racing.utilities.Constants.*;

@Getter
@Setter
@ToString
public class Car {
    private String brand;
    private double mass;
    private Point position;
    private Vector speed;
    private double heading;
    private Vector acceleration;
    private Propulsion propulsion;
    private List<Force> forces;
    private Motor motor;

    public Car(String brand, Motor motor, double mass) {
        this.brand = brand;
        this.motor = motor;
        this.mass = mass;
        this.position = new Point(0,0);
        this.speed = Vector.init();
        this.acceleration = Vector.init();
        this.forces = new ArrayList<>();
        this.propulsion = new Propulsion(this);
    }


    public void start(){
        double tractionForce = motor.couple()*R_ROUE/R_TRANSMISSION;
        double constantAcceleration = tractionForce/mass;
        // first way to start, take the direction of vehicle
        // initialize the
        var speed = new Vector(constantAcceleration*STARTING_DURATION.getSeconds(),0);
        this.acceleration = new Vector(constantAcceleration,0);
        this.speed = speed;
        this.position = newPosition(speed,STARTING_DURATION);
        this.propulsion = new Propulsion(this, Vector.init(),0.2);
    }

    public void update(Duration duration){
        updateForces(duration);
        updateSpeedAndAcceleration(duration);
        position = newPosition(speed,duration);
    }

    private void updateSpeedAndAcceleration(Duration duration){
        this.acceleration = new Vector(
                (propulsion.getVector().x()+forces.stream().mapToDouble(force->force.getVector().x()).sum())/mass,
                (propulsion.getVector().y()+forces.stream().mapToDouble(force->force.getVector().y()).sum())/mass
        );
        this.speed = new Vector(
                speed.x()+acceleration.x()*duration.getSeconds(),
                speed.y()+acceleration.y()*duration.getSeconds()
        );
    }

    private void updateForces(Duration duration){
        this.propulsion = propulsion.update(this,duration);
        this.forces = forces.stream().map(force->force.update(this,duration)).toList();
    }


    private Point newPosition(Vector speed, Duration t){
        return new Point(
                position.x()+ t.getSeconds()*speed.x(),
                position.y()+ t.getSeconds()*speed.y()
        );
    }
}
