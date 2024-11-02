package org.racing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class TimeData {
    @JsonProperty("carBrand")
    private final String carBrand;

    @JsonProperty("lapTime")
    private final double lapTime;

    public TimeData(String carBrand, double lapTime) {
        this.carBrand = carBrand;
        this.lapTime = lapTime;
    }
}
