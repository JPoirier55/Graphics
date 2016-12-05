package com.graphics.objects;

import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import static org.ejml.ops.CommonOps.*;

/**
 * Created by Jake on 10/19/2016.
 */
public class Ray2 {
    private double stval;
    private double colorR;
    private double colorG;
    private double colorB;
    private DenseMatrix64F Lv;
    private DenseMatrix64F Dv;
    private Sphere bestSphere;
    private double bestT;
    private DenseMatrix64F bestPT;
    public Ray2(DenseMatrix64F Lv, DenseMatrix64F Dv){
        this.Lv = Lv;
        this.Dv = Dv;
        this.bestSphere = null;
        this.bestPT = new DenseMatrix64F(3,1);
        this.bestT = 1000000000;
    }

    public boolean sphereTest(Sphere sphere){
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
            double d = Math.sqrt(disc);
            double t = v - d;
            if(t < bestT && t > 0.00001){

                bestT = t;
                bestSphere = sphere;
                DenseMatrix64F temp = new DenseMatrix64F(3,1);
                scale(t, Dv, temp);
                add(Lv, temp, bestPT);
                return true;
            }
        }
        return false;
    }

    public DenseMatrix64F getLv() {
        return Lv;
    }

    public DenseMatrix64F getDv() {
        return Dv;
    }

    public Sphere getBestSphere() {
        return bestSphere;
    }

    public double getBestT() {
        return bestT;
    }

    public DenseMatrix64F getBestPT() {
        return bestPT;
    }

    public double getStval() {
        return stval;
    }

    public void setStval(double stval) {
        this.stval = stval;
    }

    public double getColorR() {
        return colorR;
    }

    public void setColorR(double colorR) {
        this.colorR = colorR;
    }

    public double getColorG() {
        return colorG;
    }

    public void setColorG(double colorG) {
        this.colorG = colorG;
    }

    public double getColorB() {
        return colorB;
    }

    public void setColorB(double colorB) {
        this.colorB = colorB;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "stval=" + stval +
                ", colorR=" + colorR +
                ", colorG=" + colorG +
                ", colorB=" + colorB +
                '}';
    }
}
