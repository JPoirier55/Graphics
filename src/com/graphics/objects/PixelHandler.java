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
    private Ray ray = new Ray();
    private RayHandler rayHandler;
    public String[][] pixel_arr;
    private ArrayList stvals;
    private double[][] stArr;

    public PixelHandler(RayHandler rayHandler, Camera camera){
        this.rayHandler = rayHandler;
        this.camera = camera;
        this.maxT = 0;
        this.minT = 100000000;
    }

    public void pixels(){

        ray.UP = new DenseMatrix64F(camera.up_vect);
        ray.LV = new DenseMatrix64F(camera.lv_vect);
        ray.EV = new DenseMatrix64F(camera.ev_vect);
        ray.WV = new DenseMatrix64F(3,1);
        stvals = new ArrayList();

        subtract(ray.EV, ray.LV, ray.WV);
        divide(ray.WV,normF(ray.WV));
        ray.UV = new DenseMatrix64F(cross(new DenseMatrix64F(ray.UP), new DenseMatrix64F(ray.WV)));
        divide(ray.UV,normF(ray.UV));
        ray.VV = new DenseMatrix64F(cross(new DenseMatrix64F(ray.WV), new DenseMatrix64F(ray.UV)));

        stArr = new double[camera.resX][camera.resY];
        for(int i = 0; i < camera.resX ; i++){
            for(int j = 0; j < camera.resY; j++){
                DenseMatrix64F[] matrix_arr = pixel(i,j);
                double stval = rayHandler.shootRay(matrix_arr[0], matrix_arr[1]);
                stArr[i][j] = stval;
                stvals.add(stval);
            }
        }
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
        scale(camera.near, ray.WV, temp); // WV * near
        scale(px, ray.UV, temp2); // UV * px
        scale(py, ray.VV, temp3); // VV * py
        add(ray.EV, temp, temp4); // (WV* near) + EV
        add(temp4, temp2, temp5); // ((WV* near) + EV) + (UV * px)
        add(temp5, temp3, pixpt);// ((WV* near) + EV) + (UV * px) + (VV * py)
        subtract(pixpt, ray.EV, ray_matrix);
        divide(ray_matrix, normF(ray_matrix));
        scale(camera.near, ray_matrix, temp6);
        add(temp6, pixpt, raypt);

        DenseMatrix64F[] pts_rays = new DenseMatrix64F[]{raypt, ray_matrix};
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
