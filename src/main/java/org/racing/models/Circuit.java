package org.racing.models;

import org.racing.entities.circuit.Line;
import org.racing.physics.geometry.CoordinateSystem;

import java.util.List;

public record Circuit(List<Line> lines, CoordinateSystem system) {

}
