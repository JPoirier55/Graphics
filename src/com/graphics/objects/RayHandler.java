package com.graphics.objects;
import org.ejml.data.DenseMatrix64F;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.ejml.ops.CommonOps.*;
import static org.ejml.ops.NormOps.normF;

/**
 * Created by Jake on 10/19/2016.
 */
public class RayHandler {

    public ArrayList stvals;
    public ObjectHandler objectHandler;
    public Camera camera;
    public LightHandler lightHandler;

    public RayHandler(ObjectHandler objectHandler, Camera camera, LightHandler lightHandler) {
        this.lightHandler = lightHandler;
        this.objectHandler = objectHandler;
        this.stvals = new ArrayList();
        this.camera = camera;
    }

    public void shootRay(DenseMatrix64F Lv, DenseMatrix64F Dv, DenseMatrix64F color) {

        divide(Dv, normF(Dv));

        ArrayList temp_stvals = new ArrayList();

        for (int i = 0; i < objectHandler.getSpheres().size(); i++) {
            Sphere sphere = objectHandler.getSpheres().get(i);
            double radius = sphere.getRadius();

            //Tv = vector(RR, 3, (Cv - Lv))
            // v    = Tv.dot_product(Uv)
            // csq  = Tv.dot_product(Tv)
            // TODO: Can be optimized, stored as Densematrix to begin with
            double[][] center_v = {{sphere.getPosX()}, {sphere.getPosY()}, {sphere.getPosZ()}};
            DenseMatrix64F center = new DenseMatrix64F(center_v);
            DenseMatrix64F Tv = new DenseMatrix64F(3, 1);

            // disc = r^2 - (csq - v^2)
            subtract(center, Lv, Tv);
            double v = dot(Tv, Dv);
            double csq = dot(Tv, Tv);
            double disc = Math.pow(radius, 2) - (csq - Math.pow(v, 2));

            if (disc >= 0) {
                // d  = sqrt(disc)
                // t  = v - d
                // pt = Lv + t * Uv
                double d = Math.sqrt(disc);
                double t = v - d;
                DenseMatrix64F pt = new DenseMatrix64F(3, 1);
                DenseMatrix64F scaled_Dv = new DenseMatrix64F(3,1);
                scale(t, Dv, scaled_Dv);
                add(Lv, scaled_Dv, pt);

                // snrm = ptos - sph['c'] ; snrm = snrm / snrm.norm()
                DenseMatrix64F ptos = pt;
                DenseMatrix64F snrm = new DenseMatrix64F(3, 1);
                subtract(ptos, center, snrm);
                divide(snrm, normF(snrm));

                // TODO: can be optimized, stored as densematrix in Light object
                // color = ambient.pairwise_product(mat1['ka'])
                Light ambient = lightHandler.getAmbientLight();
                double[][] ambient_v = {{ambient.getIntensityR()}, {ambient.getIntensityG()}, {ambient.getIntensityB()}};
                DenseMatrix64F ambient_m = new DenseMatrix64F(ambient_v);
                double[][] material_v = {{sphere.getMaterialR()}, {sphere.getMaterialG()}, {sphere.getMaterialB()}};
                DenseMatrix64F material_m = new DenseMatrix64F(material_v);
                elementMult(ambient_m, material_m, color);


                for (int j = 0; j < lightHandler.getPointLights().size(); j++) {
                    // toL = ptL - ptos; toL = toL / toL.norm()
                    PointLight pointLight = lightHandler.getPointLights().get(j);
                    // TODO: can be optimized like above
                    double[][] light_v = {{pointLight.getPosX()}, {pointLight.getPosY()}, {pointLight.getPosZ()}};
                    DenseMatrix64F ptL = new DenseMatrix64F(light_v);
                    double[][] pointlight_mat_v = {{pointLight.getIntensityR()}, {pointLight.getIntensityG()}, {pointLight.getIntensityB()}};
                    DenseMatrix64F emL = new DenseMatrix64F(pointlight_mat_v);
                    DenseMatrix64F toL = new DenseMatrix64F(3, 1);
                    subtract(ptL, ptos, toL);
                    divide(toL, normF(toL));

                    if (dot(snrm, toL) > 0.0) {
                        // color += mat1['kd'].pairwise_product(emL) * snrm.dot_product(toL)
                        DenseMatrix64F diffuse = new DenseMatrix64F(3, 1);
                        elementMult(emL, material_m, diffuse);
                        DenseMatrix64F scaled_diffuse = new DenseMatrix64F(3, 1);

                        scale(dot(snrm, toL), diffuse, scaled_diffuse);
                        add(color, scaled_diffuse, color);

                        // toC    = ray['L'] - ptos; toC = toC / toC.norm()
                        DenseMatrix64F toC = new DenseMatrix64F(3, 1);
                        subtract(Lv, ptos, toC);
                        divide(toC, normF(toC));

                        // spR    = (2 * snrm.dot_product(toL) * snrm) - toL
                        DenseMatrix64F spR = new DenseMatrix64F(3, 1);
                        DenseMatrix64F scaled_snrm = new DenseMatrix64F(3, 1);
                        scale((2 * dot(snrm, toL)), snrm, scaled_snrm);
                        subtract(scaled_snrm, toL, spR);

                        // color += mat1['ks'].pairwise_product(emL) * toC.dot_product(spR)**16
                        DenseMatrix64F specular = new DenseMatrix64F(3, 1);
                        DenseMatrix64F scaled_specular = new DenseMatrix64F(3, 1);
                        elementMult(emL, material_m, specular);
                        scale(Math.pow(dot(toC, spR), 16), specular, scaled_specular);

                        add(color, scaled_specular, color);

                    }
                }
            }
        }
    }
}
//        for(int i = 0; i < objectHandler.getModels().size(); i++) {
//
//            for(int j = 0; j < objectHandler.getModels().get(i).getFaceNum(); j++) {
//                Model model = objectHandler.getModels().get(j);
//                int index_a = (int) model.getFaces().get(j).pointList.get(1);
//                int index_b = (int) model.getFaces().get(j).pointList.get(2);
//                int index_c = (int) model.getFaces().get(j).pointList.get(3);
//
//                // Get 3 points of triangle
//                double[][] av_vect = {{model.getVertices().get(index_a).getX()}, {model.getVertices().get(index_a).getY()}, {model.getVertices().get(index_a).getZ()}};
//                double[][] bv_vect = {{model.getVertices().get(index_b).getX()}, {model.getVertices().get(index_b).getY()}, {model.getVertices().get(index_b).getZ()}};
//                double[][] cv_vect = {{model.getVertices().get(index_c).getX()}, {model.getVertices().get(index_c).getY()}, {model.getVertices().get(index_c).getZ()}};
//                DenseMatrix64F Av = new DenseMatrix64F(av_vect);
//                DenseMatrix64F Bv = new DenseMatrix64F(bv_vect);
//                DenseMatrix64F Cv = new DenseMatrix64F(cv_vect);
//
//                // Get camera coordinates for look at point to triangle
//                subtract(Av, Lv, YV);
//                subtract(Av, Bv, temp1);
//                subtract(Av, Cv, temp2);
//
//                DenseMatrix64F MM = new DenseMatrix64F(3, 3);
//                for (int k = 0; k < 3; k++) {
//                    MM.set(k, 0, temp1.get(k, 0));
//                    MM.set(k, 1, temp2.get(k, 0));
//                    MM.set(k, 2, Dv.get(k, 0));
//                }
//
//                DenseMatrix64F MMs1 = new DenseMatrix64F(MM);
//                DenseMatrix64F MMs2 = new DenseMatrix64F(MM);
//                DenseMatrix64F MMs3 = new DenseMatrix64F(MM);
//                for (int k = 0; k < 3; k++) {
//                    MMs1.set(k, 0, YV.get(k, 0));
//                    MMs2.set(k, 1, YV.get(k, 0));
//                    MMs3.set(k, 2, YV.get(k, 0));
//                }
//                double detM = det(MM);
//                double detM1 = det(MMs1);
//                double detM2 = det(MMs2);
//                double detM3 = det(MMs3);
//                double stval = 0;
//                double sbeta = 0;
//                double sgamma = 0;
//
//                stval = detM3 / detM;
//                sbeta = detM1 / detM;
//                sgamma = detM2 / detM;
//
//                if (stval > 0 && sbeta >= 0 && sgamma >= 0 && (sbeta + sgamma) <= 1) {
//                    temp_stvals.add(new BigDecimal(String.valueOf(stval)).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue());
//                }
//            }
//        }
//        double minval = 0;
//        // Find the minimum value of all t vals
//        if(temp_stvals.size() >= 1){
//            minval = (double)Collections.min(temp_stvals);
//        }else{
//            minval = 0;
//        }
//
//        if(minval == 0.0){
//            minval = -1;
//        }
//
//        return minval;
//    }
//    }
