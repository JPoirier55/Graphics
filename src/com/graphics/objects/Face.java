package com.graphics.objects;

import java.util.ArrayList;

/**
 * Created by Jake on 9/3/2016.
 */
public class Face {
    public ArrayList pointList;

    public Face(){
        this.pointList = new ArrayList();
    }

    public void addPoint(int point){
        this.pointList.add(point);
    }

    @Override
    public String toString() {
        String points = "";
        for (int i = 0; i < pointList.size(); i++){
            points += pointList.get(i) + " ";
        }
        return points;
    }
}
