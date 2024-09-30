package org.racing.geometry;

import java.util.List;

public record Vector(double x, double y) {

    public static Vector init(){
        return new Vector(0, 0);
    }

    public double norm(){return Math.sqrt(x*x + y*y);}

    public double dot(Vector v){
        return x*v.x + y*v.y;
    }
    public Vector add(Vector v){
        return new Vector(x+v.x, y+v.y);
    }

    public Vector add(List<Vector> vectors){
        double newX = x+vectors.stream().mapToDouble(Vector::x).sum();
        double newY = y+vectors.stream().mapToDouble(Vector::y).sum();
        return new Vector(newX, newY);
    }

    public Vector multiply(double k){
        return new Vector(x*k, y*k);
    }
}
