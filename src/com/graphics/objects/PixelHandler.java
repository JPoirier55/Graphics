package com.graphics.objects;
import org.ejml.data.DenseMatrix64F;

import java.util.ArrayList;

import static org.ejml.ops.CommonOps.*;
import static org.ejml.ops.NormOps.normF;

/**
 * Created by Jake on 10/19/2016.
 */
public class PixelHandler {
    Camera camera;
    public double maxT, minT;
    Ray ray = new Ray();
    RayHandler rayHandler;
    public String[][] pixel_arr;
    ArrayList stvals;
    double[][] stArr;

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
//        System.out.println(ray.LV);

        subtract(ray.LV, ray.EV, ray.WV);
        divide(ray.WV,normF(ray.WV));
        System.out.println(ray.WV);
        ray.UV = new DenseMatrix64F(cross(new DenseMatrix64F(ray.UP), new DenseMatrix64F(ray.WV)));
//        System.out.println("UV: " + ray.UV);
        divide(ray.UV,normF(ray.UV));
        ray.VV = new DenseMatrix64F(cross(new DenseMatrix64F(ray.WV), new DenseMatrix64F(ray.UV)));

//        System.out.println(ray.WV);
//        System.out.println("UV:" + ray.UV);
//        System.out.println(ray.VV);

        stArr = new double[camera.resY][camera.resX];
        for(int i = 0; i < camera.resY ; i++){
            for(int j = 0; j < camera.resX; j++){
                DenseMatrix64F[] matrix_arr = pixel(i,j);
                double stval = rayHandler.shootRay(matrix_arr[0], matrix_arr[1]);
                stArr[i][j] = stval;
                stvals.add(stval);
            }
        }
    }

    public void printRGB(){
        for(int i = pixel_arr.length-1; i >=0 ; i--){
            for(int j = 0; j < pixel_arr[i].length; j++){
                System.out.print(pixel_arr[i][j] + " ");
            }
            System.out.println();
        }
    }

//    private DenseMatrix64F pixel(int i, int j){
//        double px = (double)i/(camera.width-1)*(camera.right-camera.left)+camera.left;
//        double py = (double)j/(camera.height-1)*(camera.top-camera.bottom)+camera.bottom;
////        System.out.println(px + ", "+ py);
//        DenseMatrix64F temp = new DenseMatrix64F(3,1);
//        DenseMatrix64F temp2 = new DenseMatrix64F(3,1);
//        DenseMatrix64F temp3 = new DenseMatrix64F(3,1);
//        DenseMatrix64F temp4 = new DenseMatrix64F(3,1);
//        DenseMatrix64F temp5 = new DenseMatrix64F(3,1);
//        DenseMatrix64F pixpt = new DenseMatrix64F(3,1);
//        DenseMatrix64F ray_matrix = new DenseMatrix64F(3,1);
//        scale(camera.near, ray.WV, temp); // WV * near
//        scale(px, ray.UV, temp2); // UV * px
//        scale(py, ray.VV, temp3); // VV * py
//        add(ray.EV, temp, temp4); // (WV* near) + EV
//        add(temp4, temp2, temp5); // ((WV* near) + EV) + (UV * px)
//        add(temp5, temp3, pixpt);// ((WV* near) + EV) + (UV * px) + (VV * py)
////        System.out.println(pixpt.get(0) + ", " + pixpt.get(1) + ", " + pixpt.get(2));
//
//        subtract(pixpt, ray.EV, ray_matrix);
//        return pixpt;
//    }

    private DenseMatrix64F[] pixel(int i, int j){
        double px = (double)i/(camera.width-1)*(camera.right-camera.left)+camera.left;
        double py = (double)j/(camera.height-1)*(camera.top-camera.bottom)+camera.bottom;
//        System.out.println(px + ", "+ py);
        DenseMatrix64F temp = new DenseMatrix64F(3,1);
        DenseMatrix64F temp2 = new DenseMatrix64F(3,1);
        DenseMatrix64F temp3 = new DenseMatrix64F(3,1);
        DenseMatrix64F temp4 = new DenseMatrix64F(3,1);
        DenseMatrix64F temp5 = new DenseMatrix64F(3,1);
        DenseMatrix64F pixpt = new DenseMatrix64F(3,1);
        DenseMatrix64F ray_matrix = new DenseMatrix64F(3,1);
        scale(camera.near, ray.WV, temp); // WV * near
        scale(px, ray.UV, temp2); // UV * px
        scale(py, ray.VV, temp3); // VV * py
        add(ray.EV, temp, temp4); // (WV* near) + EV
        add(temp4, temp2, temp5); // ((WV* near) + EV) + (UV * px)
        add(temp5, temp3, pixpt);// ((WV* near) + EV) + (UV * px) + (VV * py)
        subtract(ray.EV, pixpt, ray_matrix);
//        System.out.println(pixpt.get(0) + ", " + pixpt.get(1) + ", " + pixpt.get(2));

//        subtract(pixpt, ray.EV, ray_matrix);
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
        pixel_arr = new String[camera.resY][camera.resX];
        for (int i = 0; i < camera.resY; i++) {
            for (int j = 0; j < camera.resX; j++) {
//                System.out.println(stArr[i][j]);
                pixel_arr[i][j] = setPixelColor(stArr[i][j]);
            }
        }
    }


    public String setPixelColor(double stval){
        String temp ="";
//        System.out.println(stval);
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

    public double[][] cross_arr(double[][] vector1, double[][] vector2){
        double [][] vector3 = new double[3][1];
        vector3[0][0] = vector1[1][0] * vector2[2][0] - vector1[2][0] * vector2[1][0];
        vector3[1][0] = vector1[2][0] * vector2[0][0] - vector1[0][0] * vector2[2][0];
        vector3[2][0] = vector1[0][0] * vector2[1][0] - vector1[1][0] * vector2[0][0];

        return vector3;
    }

    private double[][] toArray(DenseMatrix64F matrix)
    {
        // Credit to Marco13 on SO at:
        // http://stackoverflow.com/questions/22453229/matrix-multiplication-using-ejml
        double array[][] = new double[matrix.getNumRows()][matrix.getNumCols()];
        for (int r=0; r<matrix.getNumRows(); r++)
        {
            for (int c=0; c<matrix.getNumCols(); c++)
            {
                array[r][c] = matrix.get(r,c);
            }
        }
        return array;
    }
    private void printArr(double[][] vector){
        for(int i =0; i<3; i++){
            System.out.println(vector[i][0]);
        }
    }
}
