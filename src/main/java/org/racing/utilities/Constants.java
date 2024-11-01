package org.racing.utilities;

import org.racing.entities.circuit.Race;

import java.time.Duration;

public class Constants {

    public static final double R_ROUE = 0.25;
    public static final double R_TRANSMISSION = 1.0;
    public static final Duration STARTING_DURATION = Duration.ofMillis(50);
    public static final double MILLI_TO_SECONDS = 1E-3;
    public static final Race RACE = Initializer.RACE();


}
