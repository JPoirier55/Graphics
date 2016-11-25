package com.graphics.objects;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jake on 8/30/2016.
 *
 * File manager class that handles writing and reading from files
 *
 */


public class FileManager {

    private boolean readingStrings = true;
    private String ASSETS_DIR = "C:\\Users\\Jake\\git3\\Graphics\\assets\\";

    public FileManager(){

    }

    public void writePPM(PixelHandler p, String newFilename, Camera camera){
        File outputFile = new File(newFilename);
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write("P3\n");
            writer.write(camera.resX + " " + camera.resY + " 255\n");
            for(int j = camera.resY-1; j >=0; j--){
                for (int i = 0; i<camera.resX; i++){
                    writer.write(p.pixel_arr[i][j]+" ");
                }
                writer.write("\n");
            }
            writer.close();
        } catch ( IOException e ) {
            System.err.println("ERROR: Cannot write to file: \nStack Trace: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void loadMaterialFile(Model model, String filename){
        File inFile = new File(filename);

        try {
            Scanner scan = new Scanner(inFile);
            while (scan.hasNext()) {
                String str = scan.nextLine();

                if (str.contains("newmtl")) {
                    String[] line = str.split(" ");
                    String name = line[1];
                    String[] line1 = scan.nextLine().split(" ");
                    double kar = Double.parseDouble(line1[1]);
                    double kag = Double.parseDouble(line1[2]);
                    double kab = Double.parseDouble(line1[3]);

                    String[] line2 = scan.nextLine().split(" ");
                    double kdr = Double.parseDouble(line2[1]);
                    double kdg = Double.parseDouble(line2[2]);
                    double kdb = Double.parseDouble(line2[3]);

                    String[] line3 = scan.nextLine().split(" ");
                    double ksr = Double.parseDouble(line3[1]);
                    double ksg = Double.parseDouble(line3[2]);
                    double ksb = Double.parseDouble(line3[3]);

                    model.getMaterialHandler().getMaterials().add(new Material(kar, kag, kab, kdr, kdg, kdb, ksr, ksg, ksb, name));
                }
            }
        }
        catch (FileNotFoundException e) {
            System.err.println("ERROR: File is not of proper format: \nStack Trace:");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void loadPointsObj(Model model, String filename){
        File inFile = new File(filename);

        try {
            Scanner scan = new Scanner(inFile);
            String matName = "";
            while (scan.hasNext()) {
                String str = scan.nextLine();

                if (str.contains("mtllib")) {
                    loadMaterialFile(model, ASSETS_DIR + str.split(" ")[1]);
                }
                else if (str.contains("usemtl")){
                    matName = str.split(" ")[1];

                }
                else if(str.split(" ")[0].equals("v") || str.split(" ")[0].equals("f")){
                    if(str.split(" ")[0].equals("v")){
                        String[] line = str.split(" ");
                        double vX = Double.parseDouble(line[1]);
                        double vY = Double.parseDouble(line[2]);
                        double vZ = Double.parseDouble(line[3]);
                        model.getVertices().add(new Point3D(vX, vY, vZ));
                    }else{
                        String[] line = str.split(" ");
                        int f1 = Integer.parseInt(line[1].split("/")[0]);
                        int f2 = Integer.parseInt(line[2].split("/")[0]);
                        int f3 = Integer.parseInt(line[3].split("/")[0]);

                        for(int i = 0; i < model.getMaterialHandler().getMaterials().size(); i++){
                            if(model.getMaterialHandler().getMaterials().get(i).getName().equals(matName)){
                                Face face = new Face(model.getMaterialHandler().getMaterials().get(i));
                                face.addPoint(f1);
                                face.addPoint(f2);
                                face.addPoint(f3);
                                model.getFaces().add(face);
                            }
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            System.err.println("ERROR: File is not of proper format: \nStack Trace:");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void loadSceneFile(String camera_filename, Camera camera, LightHandler lightHandler, ObjectHandler objectHandler){
        File inFile = new File(camera_filename);

        try {
            Scanner scan = new Scanner(inFile);
            while(scan.hasNext()) {
                String str = scan.nextLine();

                if (str.contains("eye")) {
                    camera.ev_vect[0][0] = Double.parseDouble(str.split(" ")[1]);
                    camera.ev_vect[1][0] = Double.parseDouble(str.split(" ")[2]);
                    camera.ev_vect[2][0] = Double.parseDouble(str.split(" ")[3]);
                }
                else if (str.contains("look")) {
                    camera.lv_vect[0][0] = Double.parseDouble(str.split(" ")[1]);
                    camera.lv_vect[1][0] = Double.parseDouble(str.split(" ")[2]);
                    camera.lv_vect[2][0] = Double.parseDouble(str.split(" ")[3]);
                }
                else if (str.contains("up")) {
                    camera.up_vect[0][0] = Double.parseDouble(str.split(" ")[1]);
                    camera.up_vect[1][0] = Double.parseDouble(str.split(" ")[2]);
                    camera.up_vect[2][0] = Double.parseDouble(str.split(" ")[3]);
                }
                else if (str.contains("bounds")) {
                    camera.left = Double.parseDouble(str.split(" ")[1]);
                    camera.bottom = Double.parseDouble(str.split(" ")[2]);
                    camera.right = Double.parseDouble(str.split(" ")[3]);
                    camera.top = Double.parseDouble(str.split(" ")[4]);
                }
                else if (str.split(" ")[0].equals("d")) {
                    camera.d = Double.parseDouble(str.split(" ")[1]);
                    camera.dv_vect[0][0] = camera.d;
                    camera.dv_vect[1][0] = camera.d;
                    camera.dv_vect[2][0] = camera.d;
                    camera.near = -camera.d;
                }
                else if (str.contains("res")){
                    camera.resX = Integer.parseInt(str.split(" ")[1]);
                    camera.resY = Integer.parseInt(str.split(" ")[2]);
                    camera.width = camera.resX;
                    camera.height = camera.resY;
                }
                else if(str.contains("ambient")){
                    lightHandler.setAmbientLight(new Light(Double.parseDouble(str.split(" ")[1]), Double.parseDouble(str.split(" ")[2]), Double.parseDouble(str.split(" ")[3])));
                }
                else if(str.contains("light")){
                    String[] line = str.split(" ");
                    lightHandler.getPointLights().add(new PointLight(Double.parseDouble(line[1]),
                            Double.parseDouble(line[2]), Double.parseDouble(line[3]),
                            Double.parseDouble(line[4]), Double.parseDouble(line[5]),
                            Double.parseDouble(line[6]), Double.parseDouble(line[7])));
                }
                else if(str.contains("sphere")){
                    String[] line = str.split(" ");
                    objectHandler.getSpheres().add(new Sphere(Double.parseDouble(line[1]),
                            Double.parseDouble(line[2]), Double.parseDouble(line[3]),
                            Double.parseDouble(line[4]), Double.parseDouble(line[5]),
                            Double.parseDouble(line[6]), Double.parseDouble(line[7])));
                }
                else if(str.contains("model")){
                    String[] line = str.split(" ");
                    Model model = new Model();
                    loadPointsObj(model, ASSETS_DIR + line[8]);
                    objectHandler.getModels().add(model);
                    System.out.println(model);
                }
            }
            scan.close();
//            for(int i =0; i < objectHandler.getSpheres().size(); i++){
//                System.out.println(objectHandler.getSpheres().get(i).toString());
//            }
        }

        catch (FileNotFoundException e) {
            System.err.println("ERROR: File is not of proper format: \nStack Trace:");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

//    public void writePoints(Model model, String newFilename){
//        File outputFile = new File(newFilename);
//        BufferedWriter writer;
//        try {
//            writer = new BufferedWriter(new FileWriter(outputFile));
//            for (int i = 0; i < model.metaData.size(); i++){
//                writer.write(model.metaData.get(i) + '\n');
//            }
//            for (int i = 0; i < model.vertices.size(); i++){
//                writer.write(model.vertices.get(i).getX() + " " +
//                        model.vertices.get(i).getY() + " " +
//                        model.vertices.get(i).getZ() + "\n");
//            }
//            for (int i = 0; i < model.faces.size(); i++){
//                writer.write(model.faces.get(i).toString() + "\n");
//            }
//            writer.close();
//        } catch ( IOException e ) {
//            System.err.println("ERROR: Cannot write to file: \nStack Trace: ");
//            e.printStackTrace();
//            System.exit(-1);
//        }
//    }

//    public void loadPoints(Model model, String filename){
//        File inFile = new File(filename);
//
//        try {
//            Scanner scan = new Scanner(inFile);
//            if(scan.hasNext()) {
//                while (this.readingStrings) {
//                    String str = scan.nextLine();
//                    model.metaData.add(str);
//                    String pattern = "([0-9]+)";
//                    Pattern p = Pattern.compile(pattern);
//                    Matcher m = p.matcher(str);
//                    if (str.contains("element vertex") && m.find()) {
//                        model.vertexNum = Integer.parseInt(m.group(0));
//                    }
//                    if (str.contains("element face") && m.find()) {
//                        model.faceNum = Integer.parseInt(m.group(0));
//                    }
//                    if (str.equals("end_header")) {
//                        this.readingStrings = false;
//                    }
//                }
//                if (model.vertexNum > 1 || model.faceNum > 1) {
//                    for (int i = 0; i < model.vertexNum; i++) {
//                        try {
//                            Double x = scan.nextDouble();
//                            Double y = scan.nextDouble();
//                            Double z = scan.nextDouble();
//                            Point3D tempPoint = new Point3D(x, y, z);
//                            model.vertices.add(tempPoint);
//                        }catch (Exception e){
//                            System.err.println("ERROR: Cannot parse vertices: \nStack Trace: ");
//                            e.printStackTrace();
//                            System.exit(-1);
//                        }
//                        scan.nextLine();
//                    }
//                    try {
//                        for (int i = 0; i < model.faceNum; i++) {
//                            Scanner str = new Scanner(scan.nextLine());
//                            Face face = new Face();
//                            int facesNum = str.nextInt();
//                            try {
//                                face.addPoint(facesNum);
//                                for (int j = 0; j < facesNum; j++) {
//                                    face.addPoint(str.nextInt());
//                                }
//                                model.faces.add(face);
//                            }catch (Exception e){
//                                System.err.println("ERROR: Cannot parse faces: \nStack Trace: " );
//                                e.printStackTrace();
//                                System.exit(-1);
//                            }
//                        }
//                        if (scan.hasNextLine()) {
//                            scan.nextLine();
//                        }
//                    }catch (Exception e){
//                        System.err.println("ERROR: Cannot parse faces: \nStack Trace: " );
//                        e.printStackTrace();
//                        System.exit(-1);
//                    }
//                }
//            }
//            scan.close();
//        }
//        catch (FileNotFoundException e) {
//            System.err.println("ERROR: File is not of proper format: \nStack Trace:");
//            e.printStackTrace();
//            System.exit(-1);
//        }
//    }