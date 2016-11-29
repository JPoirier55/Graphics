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
    public ArrayList<Ray> rays;

    public RayHandler(ObjectHandler objectHandler, Camera camera, LightHandler lightHandler) {
        this.lightHandler = lightHandler;
        this.objectHandler = objectHandler;
        this.stvals = new ArrayList();
        this.camera = camera;
    }

    public DenseMatrix64F cross(DenseMatrix64F v1, DenseMatrix64F v2){
        DenseMatrix64F v3 = new DenseMatrix64F(3,1);
        v3.set(0,0, v1.get(1,0)*v2.get(2,0)-v1.get(2,0)*v2.get(1,0));
        v3.set(1,0, v1.get(2,0)*v2.get(0,0)-v1.get(0,0)*v2.get(2,0));
        v3.set(2,0, v1.get(0,0)*v2.get(1,0)-v1.get(1,0)*v2.get(0,0));
        return v3;
    }

    public void shootRay(DenseMatrix64F Lv, DenseMatrix64F Dv, Ray ray) {

        DenseMatrix64F temp1 = new DenseMatrix64F(3, 1);
        DenseMatrix64F temp2 = new DenseMatrix64F(3, 1);
        DenseMatrix64F YV = new DenseMatrix64F(3, 1);
        this.rays = new ArrayList<>();
        ArrayList<Ray> raylist = new ArrayList<>();

        divide(Dv, normF(Dv));

        double min_stval = 100000000;

        DenseMatrix64F color = new DenseMatrix64F(3, 1);

        for (int i = 0; i < objectHandler.getModels().size(); i++) {
            Model model = objectHandler.getModels().get(i);
            for (int j = 0; j < model.getFaces().size(); j++) {
//                System.out.println(model.getFaces().size());
                Ray tempray = new Ray(0);

                Face face = model.getFaces().get(j);
                int index_a = (int) face.pointList.get(0) - 1;
                int index_b = (int) face.pointList.get(1) - 1;
                int index_c = (int) face.pointList.get(2) - 1;
//                System.out.println(index_a + " " + index_b + " " + index_c);

//                double[][] normal = {{face.getNormX()}, {face.getNormY()}, {face.getNormZ()}};
//                DenseMatrix64F N = new DenseMatrix64F(normal);

                // Get 3 points of triangle
                double[][] av_vect = {{model.getVertices().get(index_a).getX()}, {model.getVertices().get(index_a).getY()}, {model.getVertices().get(index_a).getZ()}};
                double[][] bv_vect = {{model.getVertices().get(index_b).getX()}, {model.getVertices().get(index_b).getY()}, {model.getVertices().get(index_b).getZ()}};
                double[][] cv_vect = {{model.getVertices().get(index_c).getX()}, {model.getVertices().get(index_c).getY()}, {model.getVertices().get(index_c).getZ()}};

                DenseMatrix64F Av = new DenseMatrix64F(av_vect);
                DenseMatrix64F Bv = new DenseMatrix64F(bv_vect);
                DenseMatrix64F Cv = new DenseMatrix64F(cv_vect);

                DenseMatrix64F aminusb = new DenseMatrix64F(3,1);
                subtract(Av, Bv, aminusb);
                DenseMatrix64F bminusc = new DenseMatrix64F(3,1);
                subtract(Bv, Cv, bminusc);
                DenseMatrix64F N = cross(new DenseMatrix64F(aminusb), new DenseMatrix64F(bminusc));
                divide(N, normF(N));


//                System.out.println(j);
//                System.out.println(Av);
//                System.out.println(Bv);
//                System.out.println(Cv);
//                System.out.println(N);
//

                // Get camera coordinates for look at point to triangle
                subtract(Av, Lv, YV);
                subtract(Av, Bv, temp1);
                subtract(Av, Cv, temp2);


                DenseMatrix64F MM = new DenseMatrix64F(3, 3);
                for (int k = 0; k < 3; k++) {
                    MM.set(k, 0, temp1.get(k, 0));
                    MM.set(k, 1, temp2.get(k, 0));
                    MM.set(k, 2, Dv.get(k, 0));
                }

                DenseMatrix64F MMs1 = new DenseMatrix64F(MM);
                DenseMatrix64F MMs2 = new DenseMatrix64F(MM);
                DenseMatrix64F MMs3 = new DenseMatrix64F(MM);
                for (int k = 0; k < 3; k++) {
                    MMs1.set(k, 0, YV.get(k, 0));
                    MMs2.set(k, 1, YV.get(k, 0));
                    MMs3.set(k, 2, YV.get(k, 0));
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

                if (stval > 0 && sbeta >= 0 && sgamma >= 0 && (sbeta + sgamma) <= 1) {
                    if(ray.getStval() > stval) {

                        Light ambient = lightHandler.getAmbientLight();
                        double[][] ambient_v = {{ambient.getIntensityR()}, {ambient.getIntensityG()}, {ambient.getIntensityB()}};
                        DenseMatrix64F ambient_m = new DenseMatrix64F(ambient_v);

                        double[][] material_v = {{face.material.getkAr()}, {face.material.getkAg()}, {face.material.getkAb()}};
                        DenseMatrix64F material_a = new DenseMatrix64F(material_v);

                        elementMult(ambient_m, material_a, color);


                        for (int k = 0; k < lightHandler.getPointLights().size(); k++) {
                            PointLight pointLight = lightHandler.getPointLights().get(k);
//                    // TODO: can be optimized like above
                            double[][] light_v = {{pointLight.getPosX()}, {pointLight.getPosY()}, {pointLight.getPosZ()}};
                            DenseMatrix64F ptL = new DenseMatrix64F(light_v);
                            double[][] pointlight_mat_v = {{pointLight.getIntensityR()}, {pointLight.getIntensityG()}, {pointLight.getIntensityB()}};
                            DenseMatrix64F emL = new DenseMatrix64F(pointlight_mat_v);
                            DenseMatrix64F toL = new DenseMatrix64F(3, 1);
                            DenseMatrix64F scaled_Dv = new DenseMatrix64F(3, 1);
                            scale(stval, Dv, scaled_Dv);

                            double[][] material_vD = {{face.material.getkDr()}, {face.material.getkDg()}, {face.material.getkDb()}};
                            DenseMatrix64F material_d = new DenseMatrix64F(material_vD);
                            double[][] material_vS = {{face.material.getkSr()}, {face.material.getkSg()}, {face.material.getkSb()}};
                            DenseMatrix64F material_s = new DenseMatrix64F(material_vS);

                            DenseMatrix64F ptos = new DenseMatrix64F(3, 1);
                            add(Lv, scaled_Dv, ptos);
                            subtract(ptL, ptos, toL);
                            divide(toL, normF(toL));
                            if (dot(N, toL) > 0.0) {
                                // color += mat1['kd'].pairwise_product(emL) * snrm.dot_product(toL)
                                DenseMatrix64F diffuse = new DenseMatrix64F(3, 1);
                                elementMult(emL, material_d, diffuse);
                                DenseMatrix64F scaled_diffuse = new DenseMatrix64F(3, 1);

                                scale(dot(N, toL), diffuse, scaled_diffuse);
                                add(color, scaled_diffuse, color);
//                            System.out.println(color);


                                //toC    = ray['L'] - ptos; toC = toC / toC.norm()
                                DenseMatrix64F toC = new DenseMatrix64F(3, 1);
                                subtract(ptos,Lv, toC);
                                divide(toC, normF(toC));

                                // spR    = (2 * snrm.dot_product(toL) * snrm) - toL
                                DenseMatrix64F spR = new DenseMatrix64F(3, 1);
                                DenseMatrix64F scaled_snrm = new DenseMatrix64F(3, 1);
                                scale((2 * dot(N, toL)), N, scaled_snrm);
                                subtract(scaled_snrm, toL, spR);

                                // color += mat1['ks'].pairwise_product(emL) * toC.dot_product(spR)**16
                                DenseMatrix64F specular = new DenseMatrix64F(3, 1);
                                DenseMatrix64F scaled_specular = new DenseMatrix64F(3, 1);
                                elementMult(emL, material_s, specular);
                                scale(Math.pow(dot(toC, spR), 16), specular, scaled_specular);
                                add(color, scaled_specular, color);
                            }
                        }
//                        tempray.setStval(stval);
//                        tempray.setColorR(color.get(0, 0));
//                        tempray.setColorG(color.get(1, 0));
//                        tempray.setColorB(color.get(2, 0));
//                        raylist.add(tempray);
                        ray.setStval(stval);
                        ray.setColorR(color.get(0, 0));
                        ray.setColorG(color.get(1, 0));
                        ray.setColorB(color.get(2, 0));
                    }

                }
            }
        }

//        for(int i = 0; i < raylist.size(); i++){
//            if(raylist.get(i).getStval() < min_stval){
//                min_stval = raylist.get(i).getStval();
//                ray.setColorR(raylist.get(i).getColorR());
//                ray.setColorG(raylist.get(i).getColorG());
//                ray.setColorB(raylist.get(i).getColorB());
////                System.out.println(raylist.get(i).getColorR());
//            }
//        }
//        raylist.clear();




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
                DenseMatrix64F scaled_Dv = new DenseMatrix64F(3, 1);
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
                        scale(Math.pow(dot(toC, spR), 25), specular, scaled_specular);
                        add(color, scaled_specular, color);
                    }
                }

                ray.setColorR(color.get(0, 0));
                ray.setColorG(color.get(1, 0));
                ray.setColorB(color.get(2, 0));
            }
        }
    }
}

