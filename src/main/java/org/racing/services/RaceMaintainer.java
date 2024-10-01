package org.racing.services;

import org.racing.circuit.Race;
import org.racing.config.PositionHandler;
import org.racing.geometry.Point;
import org.racing.vehicles.Car;
import org.racing.vehicles.Motor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class RaceMaintainer {

    private Race race;
    private ScheduledExecutorService executor;

    public RaceMaintainer(){
        List<Car> carList = new ArrayList<Car>();
        carList.add(new Car("Mercedes", new Motor(441299,1300.0), 500.0));
        this.race= new Race(carList);
    }

    private boolean isRunning = false;

    public void startSimulation(PositionHandler positionHandler) {
        isRunning = true; // Met à jour l'état de la simulation
        executor = Executors.newScheduledThreadPool(1);
        var car = this.race.getCars().getFirst();
        if (car.getSpeed().norm()==0.0){
            car.start();
        }
        executor.scheduleAtFixedRate(() -> {
            if (isRunning) {
                try {
                    car.update(Duration.ofMillis(50));
                    positionHandler.envoyerPosition(car.getPosition().x(), car.getPosition().y());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 50, TimeUnit.MILLISECONDS);

        executor.schedule(() -> {
            if (isRunning) {
                car.breakCar(0.1,Duration.ofMillis(50));
                System.out.println("breaking !");
                System.out.println(car.getBreaking().getVector());
            }
        }, 8, TimeUnit.SECONDS);
    }



    public void stopSimulation() {
        System.out.println("We stop");
        isRunning = false; // Met à jour l'état de la simulation
    }

}
