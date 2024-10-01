package org.racing.vehicles;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.racing.forces.Breaking;
import org.racing.forces.Force;
import org.racing.forces.Friction;
import org.racing.forces.Propulsion;
import org.racing.geometry.Point;
import org.racing.geometry.Vector;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.racing.utilities.Constants.*;

@Setter
@ToString
@Getter
public class Car {
    private String brand;
    private double mass;
    private Point position;
    private Vector speed;
    private double heading;
    private Vector acceleration;
    private Propulsion propulsion;
    private Breaking breaking;
    private List<Force> forces;
    private Motor motor;
    private Point nexPoint;
    private double wheelsHeading;

    public Car(String brand, Motor motor, double mass) {
        this.brand = brand;
        this.motor = motor;
        this.mass = mass;
        this.position = new Point(0,0);
        this.speed = Vector.init();
        this.acceleration = Vector.init();
        this.forces = new ArrayList<>();
        this.forces.add(new Friction(this));
        this.propulsion = new Propulsion(this);
        this.breaking = new Breaking(this);
        this.wheelsHeading = Math.PI/2;
    }

    public Car(String brand, Motor motor, double mass, Point nexPoint) {
        this.brand = brand;
        this.motor = motor;
        this.mass = mass;
        this.position = new Point(0,0);
        this.speed = Vector.init();
        this.acceleration = Vector.init();
        this.forces = new ArrayList<>();
        this.forces.add(new Friction(this));
        this.propulsion = new Propulsion(this);
        this.breaking = new Breaking(this);
        this.wheelsHeading = Math.PI/2;
        this.nexPoint = nexPoint;
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
        accelerate(1.0,Duration.ofMillis(50));
    }

    public void update(Duration duration){
        updateForces(duration);
        updateSpeedAndAcceleration(duration);
        position = newPosition(speed,duration);
    }

    public void breakCar(double ratio, Duration duration){
        propulsion = new Propulsion(this);
        breaking = new Breaking(this,Vector.init(),ratio).update(this,duration);
//        updateSpeedAndAcceleration(duration);
    }

    public void accelerate(double ratio, Duration duration){
        propulsion = new Propulsion(this,Vector.init(),ratio).update(this,duration);
    }

    public Vector nextPointUnitVector(){
        return Vector.of(position,nexPoint).normalize();
    }

    private void updateSpeedAndAcceleration(Duration duration){
        this.acceleration = new Vector(
                (propulsion.getVector().x() + breaking.getVector().x()+forces.stream().mapToDouble(force->force.getVector().x()).sum())/mass,
                (propulsion.getVector().y()+ breaking.getVector().y()+ forces.stream().mapToDouble(force->force.getVector().y()).sum())/mass
        );
        this.speed = new Vector(
                speed.x()+acceleration.x()*duration.toMillis()*MILLI_TO_SECONDS,
                speed.y()+acceleration.y()*duration.toMillis()*MILLI_TO_SECONDS
        );
    }

    private void updateForces(Duration duration){
        this.forces = forces.stream().map(force->force.update(this,duration)).toList();
        if (speed.dot(new Vector(1,0))<0.01){
            breaking = new Breaking(this,Vector.init(),0.0);
        }
    }


    private Point newPosition(Vector speed, Duration t){
        return new Point(
                position.x()+ t.toMillis()*MILLI_TO_SECONDS*speed.x(),
                position.y()+ t.toMillis()*MILLI_TO_SECONDS*speed.y()
        );
    }
}
