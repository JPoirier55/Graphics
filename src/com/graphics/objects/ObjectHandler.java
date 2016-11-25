package com.graphics.objects;

import java.util.ArrayList;

/**
 * Created by Jake on 11/24/2016.
 */
public class ObjectHandler {
    private ArrayList<Model> models;
    private ArrayList<Sphere> spheres;

    public ObjectHandler(){
        this.models = new ArrayList<>();
        this.spheres = new ArrayList<>();
    }

    public ArrayList<Model> getModels() {
        return models;
    }

    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }

    public ArrayList<Sphere> getSpheres() {
        return spheres;
    }

    public void setSpheres(ArrayList<Sphere> spheres) {
        this.spheres = spheres;
    }

}
