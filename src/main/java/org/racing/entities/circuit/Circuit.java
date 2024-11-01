package org.racing.entities.circuit;

import org.racing.physics.geometry.CoordinateSystem;

import java.util.List;

public record Circuit(List<Line> lines, CoordinateSystem system){}
