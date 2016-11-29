package com.graphics.objects;

import java.util.ArrayList;

/**
 * Created by Jake on 11/24/2016.
 */
public class MaterialHandler {
    private ArrayList<Material> materials;

    public MaterialHandler(){
        this.materials = new ArrayList<>();
    }

    public ArrayList<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<Material> materials) {
        this.materials = materials;
    }

    @Override
    public String toString() {
        return "MaterialHandler{" +
                "materials=" + materials +
                '}';
    }
}
