package com.graphics.objects;

import java.util.ArrayList;

/**
 * Created by Jake on 9/3/2016.
 */
public class Face {
    public ArrayList pointList;
    public Material material;

    public Face(Material material){
        this.pointList = new ArrayList();
        this.material = material;
    }

    public void addPoint(int point){
        this.pointList.add(point);
    }

    @Override
    public String toString() {
        return "Face{" +
                "pointList=" + pointList +
                ", material=" + material +
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
