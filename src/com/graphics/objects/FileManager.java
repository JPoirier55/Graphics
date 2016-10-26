package com.graphics.objects;

import java.io.*;
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

    private String filename;
    private String camera_filename;

    private boolean readingStrings = true;

    public FileManager(String filename, String camera_filename){
        this.filename = filename;
        this.camera_filename = camera_filename;
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
                    writer.write(" "+ p.pixel_arr[i][j]);
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

    public void writePoints(Model model, String newFilename){
        File outputFile = new File(newFilename);
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            for (int i = 0; i < model.metaData.size(); i++){
                writer.write(model.metaData.get(i) + '\n');
            }
            for (int i = 0; i < model.vertices.size(); i++){
                writer.write(model.vertices.get(i).getX() + " " +
                        model.vertices.get(i).getY() + " " +
                        model.vertices.get(i).getZ() + "\n");
            }
            for (int i = 0; i < model.faces.size(); i++){
                writer.write(model.faces.get(i).toString() + "\n");
            }
            writer.close();
        } catch ( IOException e ) {
            System.err.println("ERROR: Cannot write to file: \nStack Trace: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void loadPoints(Model model){
        File inFile = new File(this.filename);

        try {
            Scanner scan = new Scanner(inFile);
                if(scan.hasNext()) {
                    while (this.readingStrings) {
                        String str = scan.nextLine();
                        model.metaData.add(str);
                        String pattern = "([0-9]+)";
                        Pattern p = Pattern.compile(pattern);
                        Matcher m = p.matcher(str);
                        if (str.contains("element vertex") && m.find()) {
                            model.vertexNum = Integer.parseInt(m.group(0));
                        }
                        if (str.contains("element face") && m.find()) {
                            model.faceNum = Integer.parseInt(m.group(0));
                        }
                        if (str.equals("end_header")) {
                            this.readingStrings = false;
                        }
                    }
                    if (model.vertexNum > 1 || model.faceNum > 1) {
                        for (int i = 0; i < model.vertexNum; i++) {
                            try {
                                Double x = scan.nextDouble();
                                Double y = scan.nextDouble();
                                Double z = scan.nextDouble();
                                Point3D tempPoint = new Point3D(x, y, z);
                                model.vertices.add(tempPoint);
                            }catch (Exception e){
                                System.err.println("ERROR: Cannot parse vertices: \nStack Trace: ");
                                e.printStackTrace();
                                System.exit(-1);
                            }
                            scan.nextLine();
                        }
                    try {
                        for (int i = 0; i < model.faceNum; i++) {
                            Scanner str = new Scanner(scan.nextLine());
                            Face face = new Face();
                            int facesNum = str.nextInt();
                            try {
                                face.addPoint(facesNum);
                                for (int j = 0; j < facesNum; j++) {
                                    face.addPoint(str.nextInt());
                                }
                                model.faces.add(face);
                            }catch (Exception e){
                                System.err.println("ERROR: Cannot parse faces: \nStack Trace: " );
                                e.printStackTrace();
                                System.exit(-1);
                            }
                        }
                        if (scan.hasNextLine()) {
                            scan.nextLine();
                        }
                    }catch (Exception e){
                        System.err.println("ERROR: Cannot parse faces: \nStack Trace: " );
                        e.printStackTrace();
                        System.exit(-1);
                    }
                    }
                }
            scan.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("ERROR: File is not of proper format: \nStack Trace:");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    public void loadCamera(Camera camera){
        File inFile = new File(this.camera_filename);

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
                else if (str.contains("d")) {
                    camera.d = Double.parseDouble(str.split(" ")[1]);
                    camera.dv_vect[0][0] = camera.d;
                    camera.dv_vect[1][0] = camera.d;
                    camera.dv_vect[2][0] = camera.d;
                    camera.near = camera.d;
                }
                else if (str.contains("res")){
                    camera.resX = Integer.parseInt(str.split(" ")[1]);
                    camera.resY = Integer.parseInt(str.split(" ")[2]);
                    camera.width = camera.resX;
                    camera.height = camera.resY;
                }
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("ERROR: File is not of proper format: \nStack Trace:");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
