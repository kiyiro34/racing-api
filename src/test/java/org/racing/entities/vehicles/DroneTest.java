package org.racing.entities.vehicles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DroneTest {

    @Test
    public void test_starting() {
        var motor = new Motor(441299,600.0);
        Drone drone = new Drone("Mercedes", motor, 796.0);
        drone.start();
        assertEquals(drone.getPosition().x(),4.71E-4, 1E-5);
        assertEquals(drone.getPosition().y(),2.88E-20,1E-21);
    }

}
