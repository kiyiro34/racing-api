package org.racing.geometry;

import java.util.List;

import static java.lang.Math.acos;
import static java.lang.Math.atan;

public record Vector(double x, double y) {

    public static Vector of(Point start, Point end ){
        return new Vector(end.x()- start.x(), end.y()- start.y());
    }

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

    public Vector normalize(){
        return this.multiply((1/norm()));
    }

    public double heading(Vector second){
        if (this.norm()==0 || second.norm() ==0) return 0.0;
        return acos(this.dot(second)/(this.norm()*second.norm()));
    }

    public double heading(){
        if (this.norm()==0) return 0.0;

        var north = new Vector(0,1);
        return sign(x)*acos(this.dot(north)/this.norm());
    }

    private double sign(double value){
        if (value<0) return -1.0 ;else return 1;
    }
}
