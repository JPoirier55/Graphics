package com.graphics.objects;

/**
 * Created by Jake on 11/24/2016.
 */
public class Sphere {
    private double posX;
    private double posY;
    private double posZ;
    private double radius;
    private double materialR;
    private double materialG;
    private double materialB;
    private double materialK;

    public Sphere(double posX, double posY, double posZ, double radius, double materialR, double materialG, double materialB, double materialK) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.radius = radius;
        this.materialR = materialR;
        this.materialG = materialG;
        this.materialB = materialB;
        this.materialK = materialK;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", posZ=" + posZ +
                ", radius=" + radius +
                ", materialR=" + materialR +
                ", materialG=" + materialG +
                ", materialB=" + materialB +
                ", materialK=" + materialK +
                '}';
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getMaterialR() {
        return materialR;
    }

    public void setMaterialR(double materialR) {
        this.materialR = materialR;
    }

    public double getMaterialG() {
        return materialG;
    }

    public void setMaterialG(double materialG) {
        this.materialG = materialG;
    }

    public double getMaterialB() {
        return materialB;
    }

    public void setMaterialB(double materialB) {
        this.materialB = materialB;
    }

    public double getMaterialK() {
        return materialK;
    }

    public void setMaterialK(double materialK) {
        this.materialK = materialK;
    }
}
