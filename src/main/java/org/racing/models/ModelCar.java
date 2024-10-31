package org.racing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelCar {

    @JsonProperty(value = "brand")
    public String brand;

    @JsonProperty(value = "power")
    public double power;

    @JsonProperty(value = "mass")
    public double mass;
}
