package org.racing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class TimeData {
    @JsonProperty("carBrand")
    private final String droneBrand;

    @JsonProperty("lapTime")
    private final double lapTime;

    public TimeData(String droneBrand, double lapTime) {
        this.droneBrand = droneBrand;
        this.lapTime = lapTime;
    }
}
