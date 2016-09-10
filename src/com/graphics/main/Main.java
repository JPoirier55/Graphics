package com.graphics.main;

import com.graphics.objects.*;

public class Main {

    public static void main(String[] args) {
        Model model = new Model();
        FileManager fileManager = new FileManager(args[1]);
        fileManager.loadPoints(model);

        // Before centering
        model.setMeta();
        model.setStandardDeviations();
        printManager(model, "=== Before centering ");

        // After centering
        model.centerModel();
        model.setMeta();
        model.setStandardDeviations();
        fileManager.writePoints(model, "cube_centered.ply");
        printManager(model, "=== After centering ");

        // After whitening
        model.whitenModel();
        model.setMeta();
        model.setStandardDeviations();
        printManager(model, "=== After whitening ");

        boolean debug = true;

        if (debug){
            System.out.println("Args: " + args[0] + " " + args[1]);
        }
    }

    private static void printManager(Model model, String extraStr){
        System.out.println(extraStr);
        System.out.println(model.vertexNum + " vertices, " + model.faceNum + " polygons");
        System.out.println("Mean Vertex = " + "(" + model.meanVertex + ")");
        System.out.println("Bounding Box: " + model.boundingBox);
        System.out.println("Standard Deviations: " + model.standardDeviations);
    }
}
