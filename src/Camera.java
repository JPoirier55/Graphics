package com.graphics.objects;

/**
 * Created by Jake on 10/20/2016.
 */
public class Camera {
    double[][] ev_vect;
    double[][] lv_vect;
    double[][] dv_vect;
    double[][] up_vect;
    double width, height, near, far;
    double left, right, top, bottom;
    double d;
    int resX;
    int resY;

    public Camera(){
        this.ev_vect = new double[3][1];
        this.lv_vect = new double[3][1];
        this.dv_vect = new double[3][1];
        this.up_vect = new double[3][1];

    }

    @Override
    public String toString() {
        String temp = "";

        temp += ev_vect[0][0] + " " + ev_vect[1][0] + " " +  ev_vect[2][0];
        temp += "\n";
        temp += lv_vect[0][0] + " " + lv_vect[1][0] + " " +  lv_vect[2][0];
        temp += "\n";
        temp += dv_vect[0][0] + " " + dv_vect[1][0] + " " +  dv_vect[2][0];
        temp += "\n";
        temp += up_vect[0][0] + " " + up_vect[1][0] + " " +  up_vect[2][0];
        temp += "\n";

        temp += "width = " + width + "  height = " + height + "\n";
        temp += "left = " + left + "  right = " + right + "  top = "+ top + " bottom = "+ bottom + "\n";
        temp += "res X = " + resX + "  res Y = " + resY;

        return temp;
    }

}
