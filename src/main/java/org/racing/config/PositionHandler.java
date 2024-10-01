package org.racing.config;

import org.racing.services.RaceMaintainer;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PositionHandler extends TextWebSocketHandler {

    private RaceMaintainer simulationService;
    private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public PositionHandler(RaceMaintainer simulationService) {
        this.simulationService = simulationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);  // Ajoute la session à la liste des sessions actives
        simulationService.startSimulation(this);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        simulationService.stopSimulation(); // Arrête la simulation
    }

    public void envoyerPosition(double positionX, double positionY) throws Exception {
        String message = String.format("{\"positionX\": %.2f, \"positionY\": %.2f}", positionX, positionY);
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            }
        }
    }
}

