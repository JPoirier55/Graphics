package com.graphics.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Jake on 8/30/2016.
 */
public class FileManager {

    private String filename;

    private boolean readingStrings = true;

    public FileManager(String filename){
        this.filename = filename;
        System.out.println(filename);
    }

    public void loadPoints(ArrayList<Point3D> vertices, ArrayList<String> plyMeta){
        File inFile = new File(this.filename);

        try {
            Scanner scan = new Scanner(inFile);
            while (scan.hasNextLine()) {
                while (readingStrings) {
                    String str = scan.nextLine();
                    plyMeta.add(str);
                    if (str.equals("end_header")) {
                        readingStrings = false;
                    }
                }
                if (scan.hasNextDouble()) {
                    Double x = scan.nextDouble();
                    Double y = scan.nextDouble();
                    Double z = scan.nextDouble();
                    Point3D tempPoint = new Point3D(x, y, z);
                    vertices.add(tempPoint);
                } else scan.nextLine();
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
