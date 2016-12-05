package com.graphics.objects;
import org.ejml.data.DenseMatrix64F;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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

    public Sphere rayFind(Ray2 ray) {
        for (int i = 0; i < objectHandler.getSpheres().size(); i++) {
            ray.sphereTest(objectHandler.getSpheres().get(i));
        }
        return ray.getBestSphere();
    }

    public DenseMatrix64F rayTrace(Ray2 ray, DenseMatrix64F accum, DenseMatrix64F refatt, int level){
        if(rayFind(ray) != null){
            double[][] center_v = {{ray.getBestSphere().getPosX()}, {ray.getBestSphere().getPosY()}, {ray.getBestSphere().getPosZ()}};
            DenseMatrix64F center = new DenseMatrix64F(center_v);
            DenseMatrix64F N = new DenseMatrix64F(3,1);
            subtract(ray.getBestPT(), center, N);
            DenseMatrix64F color = new DenseMatrix64F(3,1);

            Light ambient = lightHandler.getAmbientLight();
            double[][] ambient_v = {{ambient.getIntensityR()}, {ambient.getIntensityG()}, {ambient.getIntensityB()}};
            DenseMatrix64F ambient_m = new DenseMatrix64F(ambient_v);
            double[][] material_v = {{ray.getBestSphere().getMaterialR()},
                    {ray.getBestSphere().getMaterialG()},
                    {ray.getBestSphere().getMaterialB()}};
            DenseMatrix64F material_m = new DenseMatrix64F(material_v);
            elementMult(ambient_m, material_m, color);

            for(int i = 0; i < lightHandler.getPointLights().size(); i++){
                PointLight pointLight = lightHandler.getPointLights().get(i);
                // TODO: can be optimized like above
                double[][] light_v = {{pointLight.getPosX()}, {pointLight.getPosY()}, {pointLight.getPosZ()}};
                DenseMatrix64F ptL = new DenseMatrix64F(light_v);
                double[][] pointlight_mat_v = {{pointLight.getIntensityR()}, {pointLight.getIntensityG()}, {pointLight.getIntensityB()}};
                DenseMatrix64F emL = new DenseMatrix64F(pointlight_mat_v);

                DenseMatrix64F toL = new DenseMatrix64F(3, 1);
                subtract(ptL, ray.getBestPT(), toL);
                divide(toL, normF(toL));
                if (dot(N, toL) > 0.0) {
                    // color += mat1['kd'].pairwise_product(emL) * snrm.dot_product(toL)
                    DenseMatrix64F diffuse = new DenseMatrix64F(3, 1);
                    elementMult(emL, material_m, diffuse);
                    DenseMatrix64F scaled_diffuse = new DenseMatrix64F(3, 1);

                    scale(dot(N, toL), diffuse, scaled_diffuse);
                    add(color, scaled_diffuse, color);

                    // toC    = ray['L'] - ptos; toC = toC / toC.norm()
                    DenseMatrix64F toC = new DenseMatrix64F(3, 1);
                    subtract(ray.getLv(), ray.getBestPT(), toC);
                    divide(toC, normF(toC));

                    // spR    = (2 * snrm.dot_product(toL) * snrm) - toL
                    DenseMatrix64F spR = new DenseMatrix64F(3, 1);
                    DenseMatrix64F scaled_snrm = new DenseMatrix64F(3, 1);
                    scale((2 * dot(N, toL)), N, scaled_snrm);
                    subtract(scaled_snrm, toL, spR);
                    divide(spR, normF(spR));

                    // color += mat1['ks'].pairwise_product(emL) * toC.dot_product(spR)**16
                    DenseMatrix64F specular = new DenseMatrix64F(3, 1);
                    DenseMatrix64F scaled_specular = new DenseMatrix64F(3, 1);
                    elementMult(emL, material_m, specular);
                    scale(Math.pow(dot(toC, spR), 16), specular, scaled_specular);
                    add(color, scaled_specular, color);
                }

            }
            for(int i = 0; i < 3; i++){
                DenseMatrix64F temp = new DenseMatrix64F(3,1);
                elementMult(refatt, color, temp);
                add(accum, temp, accum);
            }
            if(level > 0){
                DenseMatrix64F uINV = new DenseMatrix64F(3,1);
                scale(-1, ray.getDv(), uINV);
                DenseMatrix64F refR = new DenseMatrix64F(3,1);
                DenseMatrix64F temp = new DenseMatrix64F(3,1);
                DenseMatrix64F scaled_N = new DenseMatrix64F(3,1);
                scale(2*dot(N, uINV), N, scaled_N);
                subtract(scaled_N, uINV, refR);
                divide(refR, normF(refR));
                DenseMatrix64F refAttScaled = new DenseMatrix64F(3,1);
                scale(ray.getBestSphere().getMaterialK(), refatt, refAttScaled);
                rayTrace(new Ray2(ray.getBestPT(), refR), accum, refAttScaled, level-1);

            }
        }
        return accum;
    }

}

