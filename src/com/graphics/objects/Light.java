package com.graphics.objects;

/**
 * Created by Jake on 11/24/2016.
 */
public class Light {
    protected double intensityR;
    protected double intensityG;
    protected double intensityB;

    public Light(double intensityR, double intensityG, double intensityB){
        this.intensityR = intensityR;
        this.intensityG = intensityG;
        this.intensityB = intensityB;
    }

    public double getIntensityR() {
        return intensityR;
    }

    public double getIntensityG() {
        return intensityG;
    }

    public double getIntensityB() {
        return intensityB;
    }

    @Override
    public String toString() {
        return "Light{" +
                "intensityR=" + intensityR +
                ", intensityG=" + intensityG +
                ", intensityB=" + intensityB +
                '}';
    }
}
