package org.racing.config;

import org.json.JSONObject;
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
        sessions.add(session);
        simulationService.startSimulation(this); // Commence la simulation
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        simulationService.stopSimulation(); // Arrête la simulation quand une session est fermée
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JSONObject json = new JSONObject(payload);
        String action = json.getString("action");

        switch (action) {
            case "start":
                simulationService.startSimulation(this);
                break;
            case "reset":
                simulationService.resetSimulation(this); // Réinitialise la simulation
                break;
            case "stop":
                simulationService.stopSimulation(); // Arrête la simulation
                break;
            default:
                System.out.println("Action non reconnue: " + action);
                break;
        }
    }


    public void envoyerPosition(double positionX, double positionY, double speedX, double speedY, double nextPointVectorX, double nextPointVectorY) throws Exception {
        // Formatage du message avec les positions et les vecteurs
        String message = String.format(
                "{\"positionX\": %.2f, \"positionY\": %.2f, \"speedX\": %.2f, \"speedY\": %.2f, \"nextPointVectorX\": %.2f, \"nextPointVectorY\": %.2f}",
                positionX, positionY, speedX, speedY, nextPointVectorX, nextPointVectorY
        );

        // Envoi aux sessions WebSocket
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            }
        }
    }

}


