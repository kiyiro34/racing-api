package org.racing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.racing.models.CarData;
import org.racing.models.TimeData;
import org.racing.services.RaceMaintainer;
import org.racing.entities.vehicles.Car;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

public class PositionHandler extends TextWebSocketHandler {

    private final RaceMaintainer simulationService;
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public PositionHandler(RaceMaintainer simulationService) {
        this.simulationService = simulationService;
    }

    @Override
    public void afterConnectionEstablished( @NonNull WebSocketSession session) {
        sessions.add(session);
        simulationService.startSimulation(this);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,@NonNull CloseStatus status) {
        sessions.remove(session);
        simulationService.stopSimulation();
    }



    public void sendPositions(Map<String, Car> cars) throws Exception {
        Map<String, CarData> messageMap = new HashMap<>();
        for (Map.Entry<String, Car> entry : cars.entrySet()) {
            Car car = entry.getValue();
            messageMap.put(car.getBrand(), new CarData(car));
        }
        String message = new ObjectMapper().writeValueAsString(messageMap);
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            }
        }
    }

    public void sendTimes(String carBrand, double lapTime) throws Exception {
        //TODO Create a model
        var timeMessage = new TimeData(carBrand,lapTime);
        String message = new ObjectMapper().writeValueAsString(timeMessage);
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            }
        }
    }


}


