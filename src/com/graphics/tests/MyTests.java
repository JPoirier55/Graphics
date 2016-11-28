package com.graphics.tests;

import static org.ejml.ops.CommonOps.*;
import static org.ejml.ops.NormOps.normF;
import static org.junit.Assert.*;
import com.graphics.objects.*;
import com.sun.org.apache.bcel.internal.generic.DNEG;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

public class MyTests {
    Camera c = new Camera();
    Model m = new Model();
    ObjectHandler objectHandler = new ObjectHandler();
    LightHandler lightHandler = new LightHandler();
    RayHandler r = new RayHandler(objectHandler, c, lightHandler);
    PixelHandler p = new PixelHandler(r, c);
    @Test
    public void testPixel1(){
        p.minT = .1675476;
        p.maxT = .6654754;
        String t = p.setPixelColor(.52342543);
        System.out.println(t);
    }
    @Test
    public void testPixel2(){
        p.minT = 5;
        p.maxT = 10;
        String t = p.setPixelColor(8);
        System.out.println(t);
        assertEquals(t, "0 204 51");
    }
    @Test
    public void testDeterminant(){
        DenseMatrix64F d = new DenseMatrix64F(2,2);
        d.set(0,0,5);
        d.set(0,1,6);
        d.set(1,0,2);
        d.set(1,1,4);
        double det = det(d);
        System.out.println(det);
        assertEquals(8.0, det, 0.000001);
    }
    @Test
    public void testMin(){
        ArrayList temp_stvals = new ArrayList();
        temp_stvals.add(15.174428075870296);
        temp_stvals.add(15.174428075870296);
        temp_stvals.add(15.189233001291862);
        temp_stvals.add(15.189233001291862);
        temp_stvals.add(15.206487196640863);
        temp_stvals.add(15.206487196640863);
        temp_stvals.add(15.226182335436766);
        temp_stvals.add(15.226182335436766);
        temp_stvals.add(15.248308959311732);
        temp_stvals.add(15.248308959311732);

        double min = (double)Collections.min(temp_stvals);
        System.out.println(min);
    }
    @Test
    public void testCross(){
        double[][] up = {{0}, {0}, {1}};
        double[][] eye = {{4}, {4}, {4}};
        double[][] look = {{0}, {0}, {0}};
        DenseMatrix64F UP = new DenseMatrix64F(up);
        DenseMatrix64F LV = new DenseMatrix64F(look);
        DenseMatrix64F EV = new DenseMatrix64F(eye);
        DenseMatrix64F WV = new DenseMatrix64F(3,1);
        ArrayList list = new ArrayList();

        subtract(EV, LV, WV);
        System.out.println(WV);
        divide(WV,normF(WV));
        DenseMatrix64F UV = new DenseMatrix64F(p.cross(new DenseMatrix64F(UP), new DenseMatrix64F(WV)));
        divide(UV,normF(UV));
        DenseMatrix64F VV = new DenseMatrix64F(p.cross(new DenseMatrix64F(WV), new DenseMatrix64F(UV)));
        System.out.println(UV);
        System.out.println(VV);
    }
    @Test
    public void testCross2(){
        double[][] up = {{0}, {0}, {1}};
        double[][] eye = {{4}, {4}, {4}};
        DenseMatrix64F UP = new DenseMatrix64F(up);
        DenseMatrix64F EV = new DenseMatrix64F(eye);
        DenseMatrix64F UV = new DenseMatrix64F(p.cross(new DenseMatrix64F(UP), new DenseMatrix64F(EV)));
        System.out.println(UV);

    }
    //    @Test
//    public void testModelLoad(){
//        FileManager fileManager = new FileManager();
//        Model model = new Model();
//        Camera camera = new Camera();
//
////        fileManager.loadPoints(model, "");
//
//        DenseMatrix64F temp1 = new DenseMatrix64F(3, 1);
//        DenseMatrix64F temp2 = new DenseMatrix64F(3, 1);
//        DenseMatrix64F YV = new DenseMatrix64F(3, 1);
//        double[][] dv = {{2}, {4}, {2}};
//        DenseMatrix64F Dv = new DenseMatrix64F(dv);
//        double[][] lv = {{0}, {0}, {0}};
//        DenseMatrix64F Lv = new DenseMatrix64F(lv);
//        divide(Dv, normF(Dv));
//
//        for(int i = 0; i < model.faceNum; i++) {
//            int index_a = (int) model.faces.get(i).pointList.get(1);
//            int index_b = (int) model.faces.get(i).pointList.get(2);
//            int index_c = (int) model.faces.get(i).pointList.get(3);
//
//            double[][] av_vect = {{model.vertices.get(index_a).getX()}, {model.vertices.get(index_a).getY()}, {model.vertices.get(index_a).getZ()}};
//            double[][] bv_vect = {{model.vertices.get(index_b).getX()}, {model.vertices.get(index_b).getY()}, {model.vertices.get(index_b).getZ()}};
//            double[][] cv_vect = {{model.vertices.get(index_c).getX()}, {model.vertices.get(index_c).getY()}, {model.vertices.get(index_c).getZ()}};
//            DenseMatrix64F Av = new DenseMatrix64F(av_vect);
//            DenseMatrix64F Bv = new DenseMatrix64F(bv_vect);
//            DenseMatrix64F Cv = new DenseMatrix64F(cv_vect);
//
//            subtract(Av, Lv, YV);
//            subtract(Av, Bv, temp1);
//            subtract(Av, Cv, temp2);
//
//            DenseMatrix64F MM = new DenseMatrix64F(3,3);
//            for(int j = 0; j < 3;j++){
//                MM.set(j,0, temp1.get(j,0));
//                MM.set(j,1, temp2.get(j,0));
//                MM.set(j,2, Dv.get(j,0));
//            }
//
//            DenseMatrix64F MMs1 = new DenseMatrix64F(MM);
//            DenseMatrix64F MMs2 = new DenseMatrix64F(MM);
//            DenseMatrix64F MMs3 = new DenseMatrix64F(MM);
//            for(int j = 0; j < 3;j++){
//                MMs1.set(j, 0, YV.get(j,0));
//                MMs2.set(j, 1, YV.get(j,0));
//                MMs3.set(j, 2, YV.get(j,0));
//            }
//            double detM = det(MM);
//            double detM1 = det(MMs1);
//            double detM2 = det(MMs2);
//            double detM3 = det(MMs3);
//            double stval = 0;
//            double sbeta = 0;
//            double sgamma = 0;
//
//            stval = detM3 / detM;
//            sbeta = detM1 / detM;
//            sgamma = detM2 / detM;
//            System.out.println(stval);
//            System.out.println(sbeta);
//            System.out.println(sgamma);
//        }
//    }
    @Test
    public void testLoadSceneMoreComplex(){
        FileManager fileManager = new FileManager();
        Camera camera = new Camera();
        LightHandler lightHandler = new LightHandler();
        ObjectHandler objectHandler = new ObjectHandler();
        fileManager.loadSceneFile("C:\\Users\\Jake\\git3\\Graphics\\src\\com\\graphics\\tests\\test_scene2.txt", camera, lightHandler, objectHandler);
    }
    @Test
    public void testLoadScene(){
        FileManager fileManager = new FileManager();
        Camera camera = new Camera();
        LightHandler lightHandler = new LightHandler();
        ObjectHandler objectHandler = new ObjectHandler();
        fileManager.loadSceneFile("C:\\Users\\Jake\\git3\\Graphics\\assets\\scenes\\scene3.txt", camera, lightHandler, objectHandler);
    }
    @Test
    public void testFileLoad(){
        FileManager fileManager = new FileManager();
        Model model = new Model();
        fileManager.loadPointsObj(model, "C:\\Users\\Jake\\git3\\Graphics\\assets\\ColoredCube.obj");
        System.out.println(model);
    }

    @Test
    public void testMatFileLoad(){
        FileManager fileManager = new FileManager();
        Model model = new Model();
        fileManager.loadMaterialFile(model, "C:\\Users\\Jake\\git3\\Graphics\\assets\\ColoredCube.mtl");
        System.out.println(model);
    }
    @Test
    public void testShootRay(){
        FileManager fileManager = new FileManager();
        Camera camera = new Camera();
        LightHandler lightHandler = new LightHandler();
        ObjectHandler objectHandler = new ObjectHandler();
        RayHandler rayHandler = new RayHandler(objectHandler, camera, lightHandler);
//        fileManager.loadSceneFile("C:\\Users\\Jake\\git3\\Graphics\\src\\com\\graphics\\tests\\test_triangle_scene.txt", camera, lightHandler, objectHandler);
        fileManager.loadSceneFile("C:\\Users\\Jake\\git3\\Graphics\\src\\com\\graphics\\tests\\test_model_scene.txt", camera, lightHandler, objectHandler);
        double[][] dv = {{1},{1},{7}};
        double[][] lv = {{0},{0},{0}};
        DenseMatrix64F color = new DenseMatrix64F(3,1);
//        rayHandler.shootRay(new DenseMatrix64F(lv), new DenseMatrix64F(dv), color);
        System.out.println(color);

    }
    @Test
    public void testElementMulti(){
        double[][] dv = {{1},{1},{7}};
        double[][] lv = {{2},{3},{5}};
        DenseMatrix64F result = new DenseMatrix64F(3,1);
        elementMult(new DenseMatrix64F(lv), new DenseMatrix64F(dv), result);
        System.out.println(result);
    }

}