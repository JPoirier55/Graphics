package com.graphics.objects;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.graphics.objects.Point3D;

/**
 * Created by Jake on 8/30/2016.
 *
 * Model class that contains the entire data for
 * a 3D model as well as the methods for manipulating
 * itself - eg whitening and centering
 *
 */
public class Model {

    private ArrayList<Point3D> vertices;
    private ArrayList<Face> faces;
    private MaterialHandler materialHandler;

    private int vertexNum;
    private int faceNum;

    public Model(){
        this.vertices = new ArrayList<>();
        this.faces = new ArrayList<>();
        this.materialHandler = new MaterialHandler();
    }

    @Override
    public String toString() {
        return "Model{" +
                "vertices=" + vertices +
                ", faces=" + faces +
                ", materialHandler=" + materialHandler +
                ", vertexNum=" + vertexNum +
                ", faceNum=" + faceNum +
                '}';
    }

    public ArrayList<Point3D> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Point3D> vertices) {
        this.vertices = vertices;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public void setFaces(ArrayList<Face> faces) {
        this.faces = faces;
    }

    public MaterialHandler getMaterialHandler() {
        return materialHandler;
    }

    public void setMaterialHandler(MaterialHandler materialHandler) {
        this.materialHandler = materialHandler;
    }

    public int getVertexNum() {
        return vertexNum;
    }

    public void setVertexNum(int vertexNum) {
        this.vertexNum = vertexNum;
    }

    public int getFaceNum() {
        return faceNum;
    }

    public void setFaceNum(int faceNum) {
        this.faceNum = faceNum;
    }
    //    public void setMeta(){
//        double xSum = 0;
//        double ySum = 0;
//        double zSum = 0;
//
//        double xMin = 0;
//        double yMin = 0;
//        double zMin = 0;
//        double xMax = 0;
//        double yMax = 0;
//        double zMax = 0;
//
//        for(int i = 0; i < vertices.size(); i++) {
//            Point3D current = vertices.get(i);
//            xSum += current.getX();
//            ySum += current.getY();
//            zSum += current.getZ();
//            if(current.getX() > xMax) xMax = current.getX();
//            if(current.getY() > yMax) yMax = current.getY();
//            if(current.getZ() > zMax) zMax = current.getZ();
//            if(current.getX() < xMin) xMin = current.getX();
//            if(current.getY() < yMin) yMin = current.getY();
//            if(current.getZ() < zMin) zMin = current.getZ();
//
//        }
//        xAve = xSum / vertices.size();
//        yAve = ySum / vertices.size();
//        zAve = zSum / vertices.size();
//
//        meanVertex = new Point3D((double)Math.round(xAve*1000000d)/1000000d,
//                (double)Math.round(yAve*1000000d)/1000000d,
//                (double)Math.round(zAve*1000000d)/1000000d);
//        String xBounds = xMin + " <= x <= " + xMax;
//        String yBounds = yMin + " <= y <= " + yMax;
//        String zBounds = zMin + " <= z <=" + zMax;
//        boundingBox = xBounds + ", " + yBounds + ", " + zBounds;
//    }
//
//    public void setStandardDeviations(){
//        double xTemp = 0;
//        double yTemp = 0;
//        double zTemp = 0;
//        for(int i = 0; i < vertices.size(); i++) {
//            Point3D current = vertices.get(i);
//            xTemp += Math.pow((current.getX() - xAve), 2);
//            yTemp += Math.pow((current.getY() - yAve), 2);
//            zTemp += Math.pow((current.getZ() - zAve), 2);
//        }
//        xTemp /= vertices.size();
//        yTemp /= vertices.size();
//        zTemp /= vertices.size();
//
//        xStd = (double)Math.round(Math.sqrt(xTemp)*1000000d)/1000000d;
//        yStd = (double)Math.round(Math.sqrt(yTemp)*1000000d)/1000000d;
//        zStd = (double)Math.round(Math.sqrt(zTemp)*1000000d)/1000000d;
//
//        standardDeviations = "x = " + xStd + ", y = " + yStd + ", z = " + zStd;
//    }
//
//    public void centerModel(){
//        for(int i = 0; i < vertices.size(); i++) {
//            Point3D current = vertices.get(i);
//            double tempX = (double)Math.round((current.getX() - xAve)*1000000d)/1000000d;
//            double tempY = (double)Math.round((current.getY() - yAve)*1000000d)/1000000d;
//            double tempZ = (double)Math.round((current.getZ() - zAve)*1000000d)/1000000d;
//            centerVertices.add(new Point3D(tempX, tempY, tempZ));
//        }
//        vertices = centerVertices;
//    }
//
//    public void whitenModel(){
//        double xPercent = (xStd - 1) / xStd;
//        double yPercent = (yStd - 1) / yStd;
//        double zPercent = (zStd - 1) / zStd;
//
//        for(int i = 0; i < vertices.size(); i++) {
//            Point3D current = vertices.get(i);
//            double tempX = (double)Math.round((current.getX() - (current.getX() * xPercent))*1000000d)/1000000d;
//            double tempY = (double)Math.round((current.getY() - (current.getY() * yPercent))*1000000d)/1000000d;
//            double tempZ = (double)Math.round((current.getZ() - (current.getZ() * zPercent))*1000000d)/1000000d;
//            whitenedVertices.add(new Point3D(tempX, tempY, tempZ));
//        }
//        vertices = whitenedVertices;
//
//    }
}
