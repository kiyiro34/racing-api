package entities.vehicles;

import org.junit.jupiter.api.Test;
import org.racing.entities.vehicles.Car;
import org.racing.entities.vehicles.Motor;

import java.time.Duration;
import java.time.Instant;

public class CarTest {

    @Test
    public void test_starting() {
        var motor = new Motor(441299,600.0);
        Car car = new Car("Mercedes", motor, 796.0);
        car.start();
        System.out.println(car);
    }

    //@Test
    //public void test_running(){
    //  var motor = new Motor(441299,600.0);
    //  Car car = new Car("Mercedes", motor, 796.0);
    //  Instant start = Instant.EPOCH;
    //  Duration step = Duration.ofSeconds(1);
    //  car.start();
    //  for (int i = 1; i < 50; i++) {
    //      Instant t_i = start.plus(step.multipliedBy(i));
    //      car.update(step);
//
    //          System.out.println("time:" + t_i.getEpochSecond() +"\n");
    //      System.out.println("propulsion:" + car.getPropulsion().getVector()+"\n");
    //      System.out.println("acceleration:" + car.getAcceleration()+"\n");
    //      System.out.println("speed:" + car.getSpeed()+"\n");
    //      System.out.println("position:" + car.getPosition()+"\n");

       // }

    //}

}
