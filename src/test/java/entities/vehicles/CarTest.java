package entities.vehicles;

import org.junit.jupiter.api.Test;
import org.racing.entities.vehicles.Car;
import org.racing.entities.vehicles.Motor;

public class CarTest {

    @Test
    public void test_starting() {
        var motor = new Motor(441299,600.0);
        Car car = new Car("Mercedes", motor, 796.0);
        car.start();
        System.out.println(car);
    }

}
