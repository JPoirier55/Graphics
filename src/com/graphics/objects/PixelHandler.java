package com.graphics.objects;
import org.ejml.data.DenseMatrix64F;

import static org.ejml.ops.CommonOps.*;
import static org.ejml.ops.NormOps.normF;

/**
 * Created by Jake on 10/19/2016.
 */
public class PixelHandler {
    Camera camera;
    Ray ray = new Ray();
    RayHandler rayHandler;
    public PixelHandler(RayHandler rayHandler, Camera camera){
        this.rayHandler = rayHandler;
        this.camera = camera;
    }
    public void pixels(){

        ray.UP = new DenseMatrix64F(camera.up_vect);
        ray.LV = new DenseMatrix64F(camera.lv_vect);
        ray.EV = new DenseMatrix64F(camera.ev_vect);
        ray.WV = new DenseMatrix64F(3,1);
//        System.out.println("EV: " + ray.EV);
//        System.out.println("LV: " + ray.LV);
//        System.out.println("UP: " + ray.UP);

        subtract(ray.EV, ray.LV, ray.WV);
//        System.out.println("WV after sub: " + ray.WV);
        divide(ray.WV,normF(ray.WV));
//        System.out.println("WV: " + ray.WV);
        ray.UV = new DenseMatrix64F(cross(new DenseMatrix64F(ray.UP), new DenseMatrix64F(ray.WV)));
        divide(ray.UV,normF(ray.UV));
        ray.VV = new DenseMatrix64F(cross(new DenseMatrix64F(ray.WV), new DenseMatrix64F(ray.UV)));
//        System.out.println(ray.VV);

        for(int i = 0; i < camera.resX; i++){
            for(int j = 0; j < camera.resY; j++){
                DenseMatrix64F[] temp = pixel(i,j);
                double stval = rayHandler.shootRay(temp[0], temp[1]);
                rayHandler.setPixelColor(stval);
                System.out.print(rayHandler.getPixelColor(stval) + " ");
                rayHandler.stvals.add(stval);
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
        DenseMatrix64F pixpt = new DenseMatrix64F(3,1);
        DenseMatrix64F ray_matrix = new DenseMatrix64F(3,1);
        scale((double)camera.near, ray.WV, temp);
        scale(px, ray.UV, temp2);
        scale(py, ray.VV, temp3);
        add(ray.EV, temp, temp4);
        add(temp4, temp2, temp4);
        add(temp4, temp3, pixpt);

        subtract(pixpt, ray.EV, ray_matrix);
        divide(ray_matrix, normF(ray_matrix));
//        add(pixpt, ray_matrix, ray_matrix);
        scale(camera.near, ray_matrix);
//        System.out.println("LV for ("+i+","+j+") "+ ray_matrix);
//        System.out.println(pixpt);
        DenseMatrix64F[] rayArr = new DenseMatrix64F[]{ray_matrix, pixpt};

        return rayArr;
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
