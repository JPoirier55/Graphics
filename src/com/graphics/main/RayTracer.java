package com.graphics.main;
import com.graphics.objects.*;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.CommonOps;
import org.ejml.equation.*;
import org.ejml.ops.NormOps;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Arrays;

import static org.ejml.ops.CommonOps.divide;
import static org.ejml.ops.CommonOps.subtract;
import static org.ejml.ops.NormOps.normF;

/**
 * Created by Jake on 10/18/2016.
 */
public class RayTracer {
    int near = -50, far = -200;
    int width = 8, height = 8;
    int right = 20, left = -20;
    int top = 20, bottom = -20;
    public static void main(String[] args) {
        System.out.println("helloworld");

//        Model model = new Model();
//        FileManager fileManager = new FileManager(args[0]);
//        String fileName = cleanFileName(args[0]);
//        fileManager.loadPoints(model);
        int ex = 40, ey = 8, ez = 100;
        int lx = 8, ly = 8, lz = 8;
        int upx = 0, upy = 1, upz = 0;

        double[][] up_vect = new double[][]{{upx},{upy},{upz}};
        double[][] lv_vect = new double[][]{{lx},{ly},{lz}};
        double [][] ev_vect = new double[][]{{ex},{ey},{ez}};
        DenseMatrix64F EV = new DenseMatrix64F(up_vect);
        DenseMatrix64F LV = new DenseMatrix64F(lv_vect);
        DenseMatrix64F UP = new DenseMatrix64F(ev_vect);
        DenseMatrix64F WV = new DenseMatrix64F(3,1);
        DenseMatrix64F UV;
        DenseMatrix64F VV;
        subtract(EV, LV, WV);
        System.out.println("WV Before normal: " + WV);
        divide(WV,normF(WV));
        System.out.println("WV AFter normal: " + WV);

        double[][] wv_vect = toArray(WV);
        System.out.println("wv_vect:");
        printArr(wv_vect);
        UV = new DenseMatrix64F(cross_arr(up_vect, wv_vect));
        double[][] uv_vect = toArray(UV);
        VV = new DenseMatrix64F(cross_arr(wv_vect, uv_vect));
        System.out.println("HERE ONE:\n" + WV);
        DenseMatrix64F VV2 = new DenseMatrix64F(cross(new DenseMatrix64F(wv_vect), new DenseMatrix64F(uv_vect)));
        System.out.println("HERE TWO: \n" +WV);
        DenseMatrix64F UV2 = new DenseMatrix64F(cross(new DenseMatrix64F(UV), new DenseMatrix64F(WV)));
        System.out.println("UV ="+ UV);
        System.out.println("UV ="+ UV2);
        System.out.println("VV ="+ VV);
        System.out.println("VV ="+ VV2);
        RayTracer r = new RayTracer();
        for(int i = 0; i < r.width; i++){
            for(int j = 0; j < r.height; j++){
                double[] temp = r.pixel(i,j);
                System.out.println("PIXEL: "+temp[0] + "   " + temp[1]);
            }
        }

    }
    private double[] pixel(int i, int j){
        double px = (double)i/(width-1)*(right-left)+left;
        double py = (double)j/(height-1)*(top-bottom)+bottom;
        return new double[]{px,py};
    }

    private static double[][] toArray(DenseMatrix64F matrix)
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

    public static void printArr(double[][] vector){
        for(int i =0; i<3; i++){
            System.out.println(vector[i][0]);
        }
    }
    public static DenseMatrix64F cross(DenseMatrix64F v1, DenseMatrix64F v2){
        DenseMatrix64F v3 = new DenseMatrix64F(3,1);
        System.out.println("HERE THREEE: \n" +v1);
        v3.set(0,0, v1.get(1,0)*v2.get(2,0)-v1.get(2,0)*v2.get(1,0));
        v3.set(1,0, v1.get(2,0)*v2.get(0,0)-v1.get(0,0)*v2.get(2,0));
        v3.set(2,0, v1.get(0,0)*v2.get(1,0)-v1.get(1,0)*v2.get(0,0));
        return v3;
    }

    public static double[][] cross_arr(double[][] vector1, double[][] vector2){
        double [][] vector3 = new double[3][1];
        vector3[0][0] = vector1[1][0] * vector2[2][0] - vector1[2][0] * vector2[1][0];
        vector3[1][0] = vector1[2][0] * vector2[0][0] - vector1[0][0] * vector2[2][0];
        vector3[2][0] = vector1[0][0] * vector2[1][0] - vector1[1][0] * vector2[0][0];

        return vector3;
    }

    private static int usage(){
        System.err.println("USAGE: java Geonorm <filename>");
        return -1;
    }

    private static String cleanFileName(String fileName){
        fileName = fileName.split("/")[fileName.split("/").length-1];
        fileName = fileName.replace(".ply", "");
        return fileName;
    }
}
