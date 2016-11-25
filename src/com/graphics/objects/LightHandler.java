package com.graphics.objects;

import java.util.ArrayList;

/**
 * Created by Jake on 11/24/2016.
 */
public class LightHandler {
    private Light ambientLight;
    private ArrayList<PointLight> pointLights;

    public LightHandler(){
        this.pointLights = new ArrayList<>();
    }

    public Light getAmbientLight() {
        return ambientLight;
    }

    public ArrayList<PointLight> getPointLights() {
        return pointLights;
    }

    public void setAmbientLight(Light ambientLight) {
        this.ambientLight = ambientLight;
    }

    public void setPointLights(ArrayList<PointLight> pointLights) {
        this.pointLights = pointLights;
    }
}
