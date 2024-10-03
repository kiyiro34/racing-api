package org.racing.geometry;

public record Point(double x, double y) {

    public Point project(Vector vector){
        return new Point(x+vector.x(), y+vector.y());
    }

    public double distance(Point second){
        return Math.sqrt(Math.pow(x-second.x,2)+Math.pow(y-second.y,2));
    }
}
