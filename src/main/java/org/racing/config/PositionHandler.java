package org.racing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.racing.services.RaceMaintainer;
import org.racing.entities.vehicles.Car;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

//TODO REFACTOR HANDLER STRUCTURE
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

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        JSONObject json = new JSONObject(payload);
        String action = json.getString("action");

        switch (action) {
            case "start":
                simulationService.startSimulation(this);
                break;
            case "reset":
                simulationService.reset(this);
                break;
            case "stop":
                simulationService.stopSimulation();
                break;
            default:
                System.out.println("Not recognized: " + action);
                break;
        }
    }

    public void sendPositions(Map<String, Car> cars) throws Exception {
        Map<String, Object> messageMap = new HashMap<>();
        for (Map.Entry<String, Car> entry : cars.entrySet()) {
            Car car = entry.getValue();
            messageMap.put(car.getBrand(), getCarData(car));
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

    private Map<String, Double> getCarData(Car car) {
        Map<String, Double> carData = new HashMap<>();
        carData.put("positionX", car.getPosition().x());
        carData.put("positionY", car.getPosition().y());
        carData.put("speedX", car.getSpeed().x());
        carData.put("speedY", car.getSpeed().y());
        carData.put("nextPointVectorX", car.nextPointUnitVector().x());
        carData.put("nextPointVectorY", car.nextPointUnitVector().y());
        carData.put("heading", car.getHeading());
        carData.put("couple", car.getMotor().couple());
        carData.put("mass", car.getMass());
        return carData;
    }

    public void sendTimes(String carBrand, double lapTime) throws Exception {
        Map<String, Object> timeMessage = new HashMap<>();
        timeMessage.put("carBrand", carBrand);
        timeMessage.put("lapTime", lapTime);
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


