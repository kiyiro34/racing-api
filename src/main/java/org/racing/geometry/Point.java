package org.racing.geometry;

public record Point(double x, double y) {

    public Point project(Vector vector){
        return new Point(x+vector.x(), y+vector.y());
    }
}
