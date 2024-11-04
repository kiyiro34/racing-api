package org.racing.services;

import org.racing.entities.circuit.Race;
import org.racing.config.PositionHandler;
import org.racing.physics.geometry.Point;
import org.racing.entities.vehicles.Car;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.racing.utilities.Constants.PERIOD;
import static org.racing.utilities.Constants.RACE;

@Service
public class RaceMaintainer {
    private Race race;
    private ScheduledExecutorService executor;
    private boolean isRunning = false;

    public RaceMaintainer() {
        this.race = RACE;
    }

    private void resetRace() {
        isRunning = false;
        this.race = Initializer.RACE();
    }

    public void startSimulation(PositionHandler positionHandler) {
        if (isRunning) return;

        isRunning = true;
        executor = Executors.newScheduledThreadPool(1);
        start(positionHandler);
        executor.scheduleAtFixedRate(() -> {
            if (isRunning) {
                try {
                    race.cars().forEach(car ->{
                        car.update(PERIOD);
                        race.updatePoint();
                        try {
                            // We check if a tour has been completed
                            checkLapCompletion(positionHandler,car);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        sendCarState(positionHandler);
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    private void start(PositionHandler positionHandler) {
        race.cars().forEach(car ->{
                    if (car.getSpeed().norm() == 0.0) {
                        car.start();
                    }
            try {
                sendCarState(positionHandler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void checkLapCompletion(PositionHandler positionHandler, Car car) throws Exception {
            if (detectTour(car)) {
                double lapTime = car.getTime();
                car.setTime(0);
                positionHandler.sendTimes(car.getBrand(), lapTime);
            }
    }

    private boolean detectTour(Car car) {
        List<Point> points = car.getPointList();
        Set<Point> uniquePoints = new HashSet<>(points);

        // Check if the number of unique points is less than the total points, meaning there are duplicates
        if (uniquePoints.size() < points.size() && uniquePoints.size()>1) {
            // Reset the point list and lap time
            car.setPointList(new ArrayList<>(List.of(car.getLastPoint())));
            // Tour detected due to duplicate points
            return true;
        }
        boolean result = points.equals(getCircuitPoints());
        if (result) {
            car.setPointList(new ArrayList<>(List.of(car.getLastPoint())));
        }
        return result;
    }

    private void sendCarState(PositionHandler positionHandler) {
        sendCarPositions(positionHandler);
    }

    private void sendCarPositions(PositionHandler positionHandler) {
        try {
            Map<String, Car> carsMap = new HashMap<>();
            for (Car car : race.cars()) {
                carsMap.put(car.getBrand(), car);
            }
            // Send all cars positions
            positionHandler.sendPositions(carsMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopSimulation() {
        isRunning = false;
        if (executor != null) {
            executor.shutdown();
        }
    }

    public List<Point> getCircuitPoints(){
        return race.points();
    }

    public void addCar(Car car){
        race.addCar(car);
    }

    public void reset() {
        stopSimulation();
        resetRace();
    }
}

