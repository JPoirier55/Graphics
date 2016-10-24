package com.graphics.objects;

import org.ejml.data.DenseMatrix64F;

import java.util.ArrayList;

import static org.ejml.ops.CommonOps.det;
import static org.ejml.ops.CommonOps.divide;
import static org.ejml.ops.CommonOps.subtract;
import static org.ejml.ops.NormOps.normF;

/**
 * Created by Jake on 10/19/2016.
 */
public class RayHandler {
    public double maxT, minT;
    private ArrayList<String> rgb;
    public ArrayList stvals;
    private Model model;

    public RayHandler(Model model){
        this.rgb = new ArrayList<String>();
        this.stvals = new ArrayList();
        this.maxT = 0;
        this.minT = 100000000;
        this.model = model;

    }

    public double shootRay(DenseMatrix64F Lv, DenseMatrix64F Dv) {

        DenseMatrix64F temp1 = new DenseMatrix64F(3, 1);
        DenseMatrix64F temp2 = new DenseMatrix64F(3, 1);
        DenseMatrix64F YV = new DenseMatrix64F(3, 1);

//        System.out.println("Lv: " + Lv);
//        System.out.println("Dv: " + Dv);
        double norm_dv = normF(Dv);
        if(norm_dv != 0){
            divide(Dv, norm_dv);
        }

//        System.out.println("Normalized Dv: " + Dv);

        for(int i = 0; i < model.faceNum; i++) {

            int index_a = (int) model.faces.get(i).pointList.get(1);
            int index_b = (int) model.faces.get(i).pointList.get(2);
            int index_c = (int) model.faces.get(i).pointList.get(3);


            double[][] av_vect = {{model.vertices.get(index_a).getX()}, {model.vertices.get(index_a).getY()}, {model.vertices.get(index_a).getZ()}};
            double[][] bv_vect = {{model.vertices.get(index_b).getX()}, {model.vertices.get(index_b).getY()}, {model.vertices.get(index_b).getZ()}};
            double[][] cv_vect = {{model.vertices.get(index_c).getX()}, {model.vertices.get(index_c).getY()}, {model.vertices.get(index_c).getZ()}};
            DenseMatrix64F Av = new DenseMatrix64F(av_vect);
            DenseMatrix64F Bv = new DenseMatrix64F(bv_vect);
            DenseMatrix64F Cv = new DenseMatrix64F(cv_vect);
//            System.out.println(Av + "   " + Bv + "   " + Cv);
            subtract(Av, Lv, YV);
            subtract(Av, Bv, temp1);
            subtract(Av, Cv, temp2);


            DenseMatrix64F MM = new DenseMatrix64F(3,3);
            for(int j = 0; j < 3;j++){
                MM.set(j,0, temp1.get(j,0));
                MM.set(j,1, temp2.get(j,0));
                MM.set(j,2, Dv.get(j,0));
            }

            DenseMatrix64F MMs1 = new DenseMatrix64F(MM);
            DenseMatrix64F MMs2 = new DenseMatrix64F(MM);
            DenseMatrix64F MMs3 = new DenseMatrix64F(MM);
            for(int j = 0; j < 3;j++){
                MMs1.set(j, 0, YV.get(j,0));
                MMs2.set(j, 1, YV.get(j,0));
                MMs3.set(j, 2, YV.get(j,0));
            }
            double detM = det(MM);
            if(detM == 0){
                return -1;
            }else{
                double detM1 = det(MMs1);
                double detM2 = det(MMs2);
                double detM3 = det(MMs3);
                double stval = 0;
                double sbeta = 0;
                double sgamma = 0;

                stval = detM3 / detM;
                sbeta = detM1 / detM;
                sgamma = detM2 / detM;

                if(stval >0 && sbeta>=0 && sgamma >=0 && (sbeta+sgamma)<=1){
                    return stval;
                }
            }
        }
        return -1;
    }
    private void setGrayPixel(){
        int red = 239;
        int blue = 239;
        int green = 239;
        String temp = red + " " + green + " " + blue;
        rgb.add(temp);
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
    public String getPixelColor(double stval){
        String temp;
        if(stval < 0){
            int red = 239;
            int blue = 239;
            int green = 239;
            temp = red + " " + green + " " + blue;
        }else{
            double ratio = 2 * (stval - minT) / (maxT - minT);
//                System.out.println("RATIO: " + ratio);
            int red = (int) Math.max(0, 255 * (1 - ratio));
            int blue = (int) Math.max(0, 255 * (ratio - 1));
            int green = 255 - red - blue;
            temp = red + " " + green + " " + blue;
        }
        return temp;
    }
    public void setPixelColor(double stval){
        String temp;
//            System.out.println("STVAL COMING IN: " + (double)stvals.get(i));
        if(stval < 0){
            int red = 239;
            int blue = 239;
            int green = 239;
            temp = red + " " + green + " " + blue;
        }else{
            double ratio = 2 * (stval - minT) / (maxT - minT);
//                System.out.println("RATIO: " + ratio);
            int red = (int) Math.max(0, 255 * (1 - ratio));
            int blue = (int) Math.max(0, 255 * (ratio - 1));
            int green = 255 - red - blue;
            temp = red + " " + green + " " + blue;
        }
        rgb.add(temp);
    }
    public void printRGB(){
        for(int i = 0; i < rgb.size(); i++){
            System.out.println(rgb.get(i));
        }
    }
}
