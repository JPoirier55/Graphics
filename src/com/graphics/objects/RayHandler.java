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

        double norm_dv = normF(Dv);
        if(norm_dv != 0){
            divide(Dv, norm_dv);
        }

        ArrayList temp_stvals = new ArrayList();

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
                    temp_stvals.add(stval);
                }
            }
        }
        double min = 10000000;
        double stval = -1;
        for(int i = 0; i < temp_stvals.size(); i++){
            if((double)temp_stvals.get(i) < min){
                stval = (double)temp_stvals.get(i);
            }
        }
        return stval;
    }
}
