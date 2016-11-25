package com.graphics.objects;

/**
 * Created by Jake on 11/24/2016.
 */
public class PointLight extends Light {
    private double posX;
    private double posY;
    private double posZ;
    private double posW;
    public PointLight(double posX, double posY, double posZ, double posW, double intensityR, double intensityG, double intensityB){
        super(intensityR, intensityG, intensityB);
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.posW = posW;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public double getPosW() {
        return posW;
    }
}
