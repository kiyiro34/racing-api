package org.racing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.racing.services.RaceMaintainer;
import org.racing.vehicles.Car;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

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


    public void envoyerPositions(Map<String, Car> cars) throws Exception {
        // Création d'une map JSON pour toutes les voitures
        Map<String, Object> messageMap = new HashMap<>();

        // Boucler sur chaque voiture dans la map cars et ajouter ses données à la map JSON
        for (Map.Entry<String, Car> entry : cars.entrySet()) {
            Car car = entry.getValue();
            Map<String, Double> carData = new HashMap<>();
            carData.put("positionX", car.getPosition().x());
            carData.put("positionY", car.getPosition().y());
            carData.put("speedX", car.getSpeed().x());
            carData.put("speedY", car.getSpeed().y());
            carData.put("nextPointVectorX", car.nextPointUnitVector().x());
            carData.put("nextPointVectorY", car.nextPointUnitVector().y());

            // Ajouter les données de la voiture à la map principale sous la clé de son modèle
            messageMap.put(car.getBrand(), carData);
        }

        // Convertir la map en JSON
        String message = new ObjectMapper().writeValueAsString(messageMap);

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


