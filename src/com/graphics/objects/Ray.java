package com.graphics.objects;

import org.ejml.data.DenseMatrix64F;

/**
 * Created by Jake on 10/19/2016.
 */
public class Ray {
    private double stval;
    private double colorR;
    private double colorG;
    private double colorB;
    public Ray(double stval){
        this.stval = stval;
    }

    public double getStval() {
        return stval;
    }

    public void setStval(double stval) {
        this.stval = stval;
    }

    public double getColorR() {
        return colorR;
    }

    public void setColorR(double colorR) {
        this.colorR = colorR;
    }

    public double getColorG() {
        return colorG;
    }

    public void setColorG(double colorG) {
        this.colorG = colorG;
    }

    public double getColorB() {
        return colorB;
    }

    public void setColorB(double colorB) {
        this.colorB = colorB;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "stval=" + stval +
                ", colorR=" + colorR +
                ", colorG=" + colorG +
                ", colorB=" + colorB +
                '}';
    }
}
