package org.racing.entities.vehicles;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.racing.physics.forces.Breaking;
import org.racing.physics.forces.Force;
import org.racing.physics.forces.Friction;
import org.racing.physics.forces.Propulsion;
import org.racing.physics.geometry.Point;
import org.racing.physics.geometry.Vector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.racing.utilities.Constants.*;

@Setter
@ToString
@Getter
public class Drone {
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
    private Point lastPoint;
    private Point nextPoint;
    private double wheelsHeading;
    private List<Point> pointList;
    private double time;

    public Drone() {
        this.position = new Point(0, 0);
        this.speed = Vector.init();
        this.acceleration = Vector.init();
        this.forces = new ArrayList<>();
        this.forces.add(new Friction(this));
        this.wheelsHeading = Math.PI / 2;
        this.pointList = new ArrayList<>();
        this.time = 0;
    }

    public Drone(String brand, Motor motor, double mass) {
        this();
        this.brand = brand;
        this.motor = motor;
        this.mass = mass;
        this.heading = speed.heading();
        this.propulsion = new Propulsion(this);
        this.breaking = new Breaking(this);
    }
    
    public void updateNextPoint(Point nextPoint){
        if (this.nextPoint!=null && this.nextPoint.equals(nextPoint)  ){
            if (position.distance(nextPoint)<30){
                this.lastPoint = nextPoint;
                this.pointList.add(nextPoint);
                breaking(BREAKING_RATIO,Duration.ofMillis(0));
            }
            else{
                accelerate(ACCELERATION_RATIO,Duration.ofMillis(50));
            }
        }
        this.nextPoint = nextPoint;
        this.wheelsHeading = Vector.of(position,nextPoint).heading();
        accelerate(ACCELERATION_RATIO,Duration.ofMillis(50));
    }

    public void start(){
        double tractionForce = motor.couple()*R_ROUE/R_TRANSMISSION;
        double constantAcceleration = tractionForce/mass;
        // first way to start, take the direction of vehicle
        // initialize the different speed and acceleration
        this.acceleration = new Vector(
                constantAcceleration*sin(wheelsHeading),
                constantAcceleration*cos(wheelsHeading)
        );
        this.speed =  new Vector(
                constantAcceleration*STARTING_DURATION.toMillis()*MILLI_TO_SECONDS*sin(wheelsHeading),
                constantAcceleration*STARTING_DURATION.toMillis()*MILLI_TO_SECONDS*cos(wheelsHeading)
        );
        this.position = newPosition(speed,STARTING_DURATION);
        accelerate(1.0,Duration.ofMillis(50));
    }

    public void update(Duration duration){
        updateForces(duration);
        updateSpeedAndAcceleration(duration);
        position = newPosition(speed,duration);
        this.time+=duration.toMillis();
    }

    public Vector nextPointUnitVector(){
        return Vector.of(position,nextPoint).normalize();
    }


    private void breaking(double ratio, Duration duration){
        propulsion = new Propulsion(this);
        breaking = new Breaking(this,Vector.init(),ratio).update(this,duration);
    }

    private void accelerate(double ratio, Duration duration){
        propulsion = new Propulsion(this,Vector.init(),ratio).update(this,duration);
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
        this.heading = speed.heading();
    }

    private void updateForces(Duration duration){
        this.forces = forces.stream().map(force->force.update(this,duration)).toList();
        if (speed.dot(nextPointUnitVector())<1){
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
