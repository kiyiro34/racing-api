package org.racing.entities.vehicles;

import org.junit.jupiter.api.Test;

public class DroneTest {

    @Test
    public void test_starting() {
        var motor = new Motor(441299,600.0);
        Drone drone = new Drone("Mercedes", motor, 796.0);
        drone.start();
        System.out.println(drone);
    }

}
