package org.racing.circuit;

import org.racing.geometry.CoordinateSystem;

import java.util.List;

public record Circuit(List<Line> lines, CoordinateSystem origin) {
}
