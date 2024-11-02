package org.racing.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.racing.entities.vehicles.Car;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class CarData {
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

    public CarData(Car car) {
        this.positionX = car.getPosition().x();
        this.positionY = car.getPosition().y();
        this.speedX = car.getSpeed().x();
        this.speedY = car.getSpeed().y();
        this.nextPointVectorX = car.nextPointUnitVector().x();
        this.nextPointVectorY = car.nextPointUnitVector().y();
        this.heading = car.getHeading();
        this.couple = car.getMotor().couple();
        this.mass = car.getMass();
    }
}

