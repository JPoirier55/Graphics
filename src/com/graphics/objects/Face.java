package com.graphics.objects;

import org.ejml.data.DenseMatrix64F;

import java.util.ArrayList;

/**
 * Created by Jake on 9/3/2016.
 */
public class Face {
    public ArrayList pointList;
    public Material material;
    private double normX;
    private double normY;
    private double normZ;

    public Face(Material material){
        this.pointList = new ArrayList();
        this.material = material;
    }

    public void addPoint(int point){
        this.pointList.add(point);
    }

    public double getNormX() {
        return normX;
    }

    public void setNormX(double normX) {
        this.normX = normX;
    }

    public double getNormY() {
        return normY;
    }

    public void setNormY(double normY) {
        this.normY = normY;
    }

    public double getNormZ() {
        return normZ;
    }

    public void setNormZ(double normZ) {
        this.normZ = normZ;
    }

    @Override
    public String toString() {
        return "Face{" +
                "pointList=" + pointList +
                ", material=" + material +
                ", normX=" + normX +
                ", normY=" + normY +
                ", normZ=" + normZ +
                '}';
    }

    //    @Override
//    public String toString() {
//        String points = "";
//        for (int i = 0; i < pointList.size(); i++){
//            points += pointList.get(i) + " ";
//        }
//        return points;
//    }
}
