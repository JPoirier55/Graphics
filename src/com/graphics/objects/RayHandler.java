package com.graphics.objects;
import org.ejml.data.DenseMatrix64F;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.ejml.ops.CommonOps.det;
import static org.ejml.ops.CommonOps.divide;
import static org.ejml.ops.CommonOps.subtract;
import static org.ejml.ops.NormOps.normF;

/**
 * Created by Jake on 10/19/2016.
 */
public class RayHandler {

    public ArrayList stvals;
    private Model model;
    public Camera camera;

    public RayHandler(Model model, Camera camera){
        this.stvals = new ArrayList();
        this.model = model;
        this.camera = camera;
    }

    public double shootRay(DenseMatrix64F Lv, DenseMatrix64F Dv) {
        DenseMatrix64F temp1 = new DenseMatrix64F(3, 1);
        DenseMatrix64F temp2 = new DenseMatrix64F(3, 1);
        DenseMatrix64F YV = new DenseMatrix64F(3, 1);

        divide(Dv, normF(Dv));

        ArrayList temp_stvals = new ArrayList();

        for(int i = 0; i < model.faceNum; i++) {

            int index_a = (int) model.faces.get(i).pointList.get(1);
            int index_b = (int) model.faces.get(i).pointList.get(2);
            int index_c = (int) model.faces.get(i).pointList.get(3);

            // Get 3 points of triangle
            double[][] av_vect = {{model.vertices.get(index_a).getX()}, {model.vertices.get(index_a).getY()}, {model.vertices.get(index_a).getZ()}};
            double[][] bv_vect = {{model.vertices.get(index_b).getX()}, {model.vertices.get(index_b).getY()}, {model.vertices.get(index_b).getZ()}};
            double[][] cv_vect = {{model.vertices.get(index_c).getX()}, {model.vertices.get(index_c).getY()}, {model.vertices.get(index_c).getZ()}};
            DenseMatrix64F Av = new DenseMatrix64F(av_vect);
            DenseMatrix64F Bv = new DenseMatrix64F(bv_vect);
            DenseMatrix64F Cv = new DenseMatrix64F(cv_vect);

            // Get camera coordinates for look at point to triangle
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
                temp_stvals.add(new BigDecimal(String.valueOf(stval)).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        double minval = 0;
        // Find the minimum value of all t vals
        if(temp_stvals.size() >= 1){
            minval = (double)Collections.min(temp_stvals);
        }else{
            minval = 0;
        }

        if(minval == 0.0){
            minval = -1;
        }

        return minval;
    }
}
