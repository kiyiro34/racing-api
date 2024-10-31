package org.racing.utilities;

import org.racing.entities.circuit.Circuit;
import org.racing.entities.circuit.Line;
import org.racing.entities.circuit.Race;
import org.racing.physics.geometry.CoordinateSystem;
import org.racing.physics.geometry.Point;
import org.racing.physics.geometry.Segment;
import org.racing.physics.geometry.Vector;
import org.racing.entities.vehicles.Car;
import org.racing.entities.vehicles.Motor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final double R_ROUE = 0.25;
    public static final double R_TRANSMISSION = 1.0;
    public static final Duration STARTING_DURATION = Duration.ofMillis(50);
    public static final double MILLI_TO_SECONDS = 1E-3;

    public static final Race RACE = Initializer.RACE();


}
