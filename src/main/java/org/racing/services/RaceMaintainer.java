package org.racing.services;

import org.racing.entities.circuit.Race;
import org.racing.socket.PositionHandler;
import org.racing.physics.geometry.Point;
import org.racing.entities.vehicles.Car;
import org.racing.utilities.Initializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
public class RaceMaintainer {

    private Race race;
    private ScheduledExecutorService executor;
    private boolean isRunning = false;

    public RaceMaintainer() {
        resetRace();
    }

    public void resetRace() {
        isRunning = false;
        this.race = Initializer.RACE();
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
                envoyerCarState(positionHandler);
            } catch (Exception e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        });



        executor.scheduleAtFixedRate(() -> {
            if (isRunning) {
                try {
                    race.getCars().forEach(car ->{
                        car.update(Duration.ofMillis(50));
                        race.updatePoint();
//                        race.checkCars(Duration.ofMillis(50));
                        try {
                            checkLapCompletion(positionHandler,car); // Vérifier si le tour est complété
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        envoyerCarState(positionHandler);
                    });
                } catch (Exception e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    private void checkLapCompletion(PositionHandler positionHandler, Car car) throws Exception {
            if (detectTour(car)) {
                double lapTime = car.getTime();
                car.setTime(0);// Récupérer le temps du tour
                positionHandler.envoyerTemps(car.getBrand(), lapTime); // Envoyer le temps au frontend
            }
    }

    private boolean detectTour(Car car) {
        List<Point> points = car.getPointList();
        Set<Point> uniquePoints = new HashSet<>(points);

        // Check if the number of unique points is less than the total points, meaning there are duplicates
        if (uniquePoints.size() < points.size() && uniquePoints.size()>1) {
            // Reset the point list and lap time
            car.setPointList(new ArrayList<>(List.of(car.getLastPoint())));
            return true; // Tour detected due to duplicate points
        }
        boolean result = points.equals(getCircuitPoints());
        if (result) {
            car.setPointList(new ArrayList<>(List.of(car.getLastPoint())));
        }
        return result;
    }

    private void envoyerCarState(PositionHandler positionHandler) {
        carPositions(positionHandler);
    }

    private void carPositions(PositionHandler positionHandler) {
        try {
            // Créer une map où chaque voiture est stockée avec son modèle comme clé
            Map<String, Car> carsMap = new HashMap<>();

            for (Car car : race.getCars()) {
                carsMap.put(car.getBrand(), car);
            }

            // Envoyer toutes les positions des voitures
            positionHandler.envoyerPositions(carsMap);
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
        return Initializer.RACE().getCircuit().lines().stream().flatMap(line -> Stream.of(line.segment().start(), line.segment().end())).toList();
    }

    public void addCar(Car car){
        race.addCar(car);
    }

    public void resetSimulation(PositionHandler positionHandler) {
        stopSimulation();
        resetRace();
        carPositions(positionHandler);
    }
}

