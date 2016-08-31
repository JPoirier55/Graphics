package com.graphics.objects;

/**
 * Created by Jake on 8/30/2016.
 */
public class Point3D {
    private double x;
    private double y;
    private double z;

    public Point3D(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Point3D(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    public String toString(){
        return String.valueOf(this.x) + "," + String.valueOf(this.y) + "," + String.valueOf(this.z);
    }

}
