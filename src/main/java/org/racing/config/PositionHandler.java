package org.racing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.racing.models.DroneData;
import org.racing.models.TimeData;
import org.racing.services.RaceMaintainer;
import org.racing.entities.vehicles.Drone;
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



    public void sendPositions(Map<String, Drone> drones) throws Exception {
        Map<String, DroneData> messageMap = new HashMap<>();
        for (Map.Entry<String, Drone> entry : drones.entrySet()) {
            Drone drone = entry.getValue();
            messageMap.put(drone.getBrand(), new DroneData(drone));
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

    public void sendTimes(String droneBrand, double lapTime) throws Exception {
        //TODO Create a model
        var timeMessage = new TimeData(droneBrand,lapTime);
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


