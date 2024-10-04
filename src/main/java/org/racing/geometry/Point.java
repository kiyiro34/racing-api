package org.racing.geometry;

public record Point(double x, double y) {

    public Point project(Vector vector){
        return new Point(x+vector.x(), y+vector.y());
    }

    public double distance(Point second){
        return Math.sqrt(Math.pow(x-second.x,2)+Math.pow(y-second.y,2));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point other = (Point) obj;
            return Double.compare(this.x, other.x) == 0 && Double.compare(this.y, other.y) == 0;
        }
        return false; // Pas un Point, donc pas égal
    }
}
