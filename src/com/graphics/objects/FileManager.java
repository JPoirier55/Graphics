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

    private boolean readingStrings = true;

    public FileManager(String filename){
        this.filename = filename;
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
            e.printStackTrace();
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
                            Double x = scan.nextDouble();
                            Double y = scan.nextDouble();
                            Double z = scan.nextDouble();
                            Point3D tempPoint = new Point3D(x, y, z);
                            model.vertices.add(tempPoint);
                            scan.nextLine();
                        }

                        for (int i = 0; i < model.faceNum; i++) {
                            Scanner str = new Scanner(scan.nextLine());
                            Face face = new Face();
                            while (str.hasNextInt()) {
                                face.addPoint(str.nextInt());
                            }
                            model.faces.add(face);
                        }
                        if (scan.hasNextLine()) {
                            scan.nextLine();
                        }
                    }
                }
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
