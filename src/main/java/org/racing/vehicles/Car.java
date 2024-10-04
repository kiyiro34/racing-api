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

import static java.lang.Math.cos;
import static java.lang.Math.sin;
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
    private Point lastPoint;
    private Point nexPoint;
    private double wheelsHeading;
    private List<Point> pointList;
    private double time;

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
        this.heading = speed.heading();
        this.pointList = new ArrayList<>();
        this.time=0;
    }

    public void updateNextPoint(Point nextPoint){
        if (this.nexPoint!=null && this.nexPoint.equals(nextPoint)  ){
            if (position.distance(nexPoint)<30){
                this.lastPoint = nextPoint;
                this.pointList.add(nextPoint);
                breakCar(1,Duration.ofMillis(0));
            }
            else{
                accelerate(1.0,Duration.ofMillis(50));
            }
        }
        this.nexPoint = nextPoint;
        this.wheelsHeading = Vector.of(position,nextPoint).heading();
        accelerate(1.0,Duration.ofMillis(50));
    }

    public void stop(Duration duration){
        this.propulsion = new Propulsion(this);
        this.forces = new ArrayList<>();
        while (speed.dot(nextPointUnitVector())<1){
            breakCar(1,Duration.ofMillis(0));
            updateForces(duration);
            updateSpeedAndAcceleration(duration);
        }
        this.breaking =new Breaking(this);
    }

    public void start(){
        double tractionForce = motor.couple()*R_ROUE/R_TRANSMISSION;
        double constantAcceleration = tractionForce/mass;
        // first way to start, take the direction of vehicle
        // initialize the
        var speed = new Vector(constantAcceleration*STARTING_DURATION.toMillis()*MILLI_TO_SECONDS*sin(wheelsHeading),constantAcceleration*STARTING_DURATION.toMillis()*MILLI_TO_SECONDS*cos(wheelsHeading));
        this.acceleration = new Vector(constantAcceleration*sin(wheelsHeading),constantAcceleration*cos(wheelsHeading));
        this.speed = speed;
        this.position = newPosition(speed,STARTING_DURATION);
        accelerate(1.0,Duration.ofMillis(50));
    }

    public void update(Duration duration){
        updateForces(duration);
        updateSpeedAndAcceleration(duration);
        position = newPosition(speed,duration);
        this.time+=duration.toMillis();
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
