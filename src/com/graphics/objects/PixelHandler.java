package com.graphics.objects;
import org.ejml.data.DenseMatrix64F;

import java.util.ArrayList;

import static org.ejml.ops.CommonOps.*;
import static org.ejml.ops.NormOps.normF;

/**
 * Created by Jake on 10/19/2016.
 */
public class PixelHandler {
    private Camera camera;
    public double maxT, minT;
    private RayHandler rayHandler;
    public String[][] pixel_arr;
    private ArrayList stvals;
    private double[][] stArr;

    public DenseMatrix64F UP;
    public DenseMatrix64F LV;
    public DenseMatrix64F EV;
    public DenseMatrix64F WV;
    public DenseMatrix64F UV;
    public DenseMatrix64F VV;

    public PixelHandler(RayHandler rayHandler, Camera camera){
        this.rayHandler = rayHandler;
        this.camera = camera;
        this.maxT = 0;
        this.minT = 100000000;
        UP = new DenseMatrix64F(camera.up_vect);
        LV = new DenseMatrix64F(camera.lv_vect);
        EV = new DenseMatrix64F(camera.ev_vect);
    }

    public void pixels(){

        WV = new DenseMatrix64F(3,1);
        stvals = new ArrayList();

        subtract(EV, LV, WV);
        divide(WV,normF(WV));
        UV = new DenseMatrix64F(cross(new DenseMatrix64F(UP), new DenseMatrix64F(WV)));
        divide(UV,normF(UV));
        VV = new DenseMatrix64F(cross(new DenseMatrix64F(WV), new DenseMatrix64F(UV)));

        pixel_arr = new String[camera.resX][camera.resY];
        for(int i = 0; i < camera.resX ; i++){
            for(int j = 0; j < camera.resY; j++){
                DenseMatrix64F[] matrix_arr = pixel(i,((int)camera.height-j-1));
                Ray ray = new Ray(1000000);

                rayHandler.shootRay(matrix_arr[0], matrix_arr[1], ray);
                double[][] color_v = {{ray.getColorR()},{ray.getColorG()}, {ray.getColorB()}};
                DenseMatrix64F color = new DenseMatrix64F(color_v);

                double[] colors = truncate_values(color.get(0,0), color.get(1,0), color.get(2,0));
                pixel_arr[i][j] = (int)(255*colors[0]) + " " + (int)(255*colors[1]) + " " + (int)(255*colors[2]);
            }
        }
    }
    private double[] truncate_values(double red, double green, double blue){
        if(red > 1){
            red = 1;
        }
        if(green > 1){
            green = 1;
        }
        if(blue > 1){
            blue = 1;
        }
        double[] colors = {red, green, blue};
        return colors;
    }

    public void printRGB(){
        for(int i = 0; i < camera.resX ; i++){
            for(int j = 0; j < camera.resY; j++){
                System.out.print(pixel_arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    private DenseMatrix64F[] pixel(int i, int j){
        double px = (double)i/(camera.width-1)*(camera.right-camera.left)+camera.left;
        double py = (double)j/(camera.height-1)*(camera.top-camera.bottom)+camera.bottom;

        DenseMatrix64F temp = new DenseMatrix64F(3,1);
        DenseMatrix64F temp2 = new DenseMatrix64F(3,1);
        DenseMatrix64F temp3 = new DenseMatrix64F(3,1);
        DenseMatrix64F temp4 = new DenseMatrix64F(3,1);
        DenseMatrix64F temp5 = new DenseMatrix64F(3,1);
        DenseMatrix64F temp6 = new DenseMatrix64F(3,1);
        DenseMatrix64F pixpt = new DenseMatrix64F(3,1);
        DenseMatrix64F raypt = new DenseMatrix64F(3,1);
        DenseMatrix64F ray_matrix = new DenseMatrix64F(3,1);
        scale(camera.near, WV, temp); // WV * near
        scale(px, UV, temp2); // UV * px
        scale(py, VV, temp3); // VV * py
        add(EV, temp, temp4); // (WV* near) + EV
        add(temp4, temp2, temp5); // ((WV* near) + EV) + (UV * px)
        add(temp5, temp3, pixpt);// ((WV* near) + EV) + (UV * px) + (VV * py)
        subtract(pixpt, EV, ray_matrix);
        divide(ray_matrix, normF(ray_matrix));

        DenseMatrix64F[] pts_rays = new DenseMatrix64F[]{pixpt, ray_matrix};
        return pts_rays;
    }

    public void getMaxes(){
        for(int i = 0 ; i< stvals.size(); i++) {
            double temp = (double)stvals.get(i);
            if (temp < minT && temp >0) {
                minT = temp;
            }
            if (temp > maxT) {
                maxT = temp;
            }
        }
    }

    public void pixelsetter() {
        pixel_arr = new String[camera.resX][camera.resY];
        for (int i = 0; i < camera.resX; i++) {
            for (int j = 0; j < camera.resY; j++) {
                pixel_arr[i][j] = setPixelColor(stArr[i][j]);
            }
        }
    }

    public String setPixelColor(double stval){
        String temp;
        if(stval < 0){
            int red = 239;
            int blue = 239;
            int green = 239;
            temp = red + " " + green + " " + blue;

        }else{

            double ratio = 2 * (stval - minT) / (maxT - minT);
            int red = (int) Math.max(0, 255 * (1 - ratio));
            int blue = (int) Math.max(0, 255 * (ratio - 1));
            int green = 255 - red - blue;
            temp = red + " " + green + " " + blue;
        }
        return temp;
    }

    public DenseMatrix64F cross(DenseMatrix64F v1, DenseMatrix64F v2){
        DenseMatrix64F v3 = new DenseMatrix64F(3,1);
        v3.set(0,0, v1.get(1,0)*v2.get(2,0)-v1.get(2,0)*v2.get(1,0));
        v3.set(1,0, v1.get(2,0)*v2.get(0,0)-v1.get(0,0)*v2.get(2,0));
        v3.set(2,0, v1.get(0,0)*v2.get(1,0)-v1.get(1,0)*v2.get(0,0));
        return v3;
    }
}
