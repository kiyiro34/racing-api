package org.racing.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.racing.entities.vehicles.Drone;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class DroneData {
    @JsonProperty("positionX")
    private final double positionX;

    @JsonProperty("positionY")
    private final double positionY;

    @JsonProperty("speedX")
    private final double speedX;

    @JsonProperty("speedY")
    private final double speedY;

    @JsonProperty("nextPointVectorX")
    private final double nextPointVectorX;

    @JsonProperty("nextPointVectorY")
    private final double nextPointVectorY;

    @JsonProperty("heading")
    private final double heading;

    @JsonProperty("couple")
    private final double couple;

    @JsonProperty("mass")
    private final double mass;

    public DroneData(Drone drone) {
        this.positionX = drone.getPosition().x();
        this.positionY = drone.getPosition().y();
        this.speedX = drone.getSpeed().x();
        this.speedY = drone.getSpeed().y();
        this.nextPointVectorX = drone.nextPointUnitVector().x();
        this.nextPointVectorY = drone.nextPointUnitVector().y();
        this.heading = drone.getHeading();
        this.couple = drone.getMotor().couple();
        this.mass = drone.getMass();
    }
}

