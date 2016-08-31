package com.graphics.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.graphics.objects.Point3D;

/**
 * Created by Jake on 8/30/2016.
 */
public class Model {
    public ArrayList<Point3D> vertices = new ArrayList<>();

    private boolean readingStrings = true;
    public void print(ArrayList arrayList){
        for(int i = 0; i <arrayList.size(); i++) {
            System.out.println(arrayList.get(i).toString());
        }
    }

    public void loadPoints(String filename){
        File inFile = new File(filename);
        try {
            Scanner scan = new Scanner(inFile);
            while (scan.hasNextLine()) {
                while (readingStrings) {
                    String str = scan.next();
                    if (str.equals("end_header")) {
                        readingStrings = false;
                    }
                }
                Double x = scan.nextDouble();
                Double y = scan.nextDouble();
                Double z = scan.nextDouble();
                Point3D tempPoint = new Point3D(x, y, z);
                this.vertices.add(tempPoint);
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
