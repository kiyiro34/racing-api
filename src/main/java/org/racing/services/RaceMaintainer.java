package org.racing.services;

import org.racing.circuit.Circuit;
import org.racing.circuit.Line;
import org.racing.circuit.Race;
import org.racing.config.PositionHandler;
import org.racing.geometry.CoordinateSystem;
import org.racing.geometry.Point;
import org.racing.geometry.Segment;
import org.racing.geometry.Vector;
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
import java.util.stream.Stream;

import static org.racing.utilities.Constants.RACE;

@Service
public class RaceMaintainer {

    private Race race;
    private ScheduledExecutorService executor;
    private boolean isRunning = false;

    public RaceMaintainer() {
        resetRace();
    }

    public void resetRace() {
        this.race = RACE;
    }

    public void startSimulation(PositionHandler positionHandler) {
        if (isRunning) return;

        isRunning = true;
        executor = Executors.newScheduledThreadPool(1);

        race.getCars().forEach(car ->{
                    if (car.getSpeed().norm() == 0.0) {
                        car.start();
                    }
            try {
                envoyerCarState(positionHandler,car);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        executor.scheduleAtFixedRate(() -> {
            if (isRunning) {
                try {
                    race.getCars().forEach(car ->{
                        car.update(Duration.ofMillis(50));
                        race.updatePoint();
                        race.checkCars(Duration.ofMillis(50));
                        envoyerCarState(positionHandler,car);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    private void envoyerCarState(PositionHandler positionHandler,Car car) {
        try {
            positionHandler.envoyerPosition(
                    car.getPosition().x(),
                    car.getPosition().y(),
                    car.getSpeed().x(),
                    car.getSpeed().y(),
                    car.nextPointUnitVector().x(),
                    car.nextPointUnitVector().y()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stopSimulation() {
        isRunning = false;
        if (executor != null) {
            executor.shutdown();
        }
    }

    public List<Point> getCircuitPoints(){
        return RACE().getCircuit().lines().stream().flatMap(line -> Stream.of(line.segment().start(), line.segment().end())).toList();
    }

    public void resetSimulation(PositionHandler positionHandler) {
        stopSimulation();
        resetRace();
        try {
            positionHandler.envoyerPosition(race.getCars().getFirst().getPosition().x(), race.getCars().getFirst().getPosition().y(),race.getCars().getFirst().getSpeed().x(),race.getCars().getFirst().getSpeed().y(),race.getCars().getFirst().nextPointUnitVector().x(),race.getCars().getFirst().nextPointUnitVector().y());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

