package com.graphics.main;

import com.graphics.objects.*;

public class Geonorm {

    public static void main(String[] args) {
        if(args.length < 1){
            System.exit(usage());
        }
        Model model = new Model();
        FileManager fileManager = new FileManager(args[0]);
        String fileName = cleanFileName(args[0]);
        fileManager.loadPoints(model);

        // Before centering
        model.setMeta();
        model.setStandardDeviations();
        printManager(model, "=== Before centering ");

        // After centering
        model.centerModel();
        model.setMeta();
        model.setStandardDeviations();
        fileManager.writePoints(model, fileName + "_centered.ply");
        printManager(model, "=== After centering ");

        // After whitening
        model.whitenModel();
        model.setMeta();
        model.setStandardDeviations();
        fileManager.writePoints(model, fileName + "_rounded.ply");
        printManager(model, "=== After whitening ");

        boolean debug = false;

        if (debug){
            System.out.println("Args: " + args[0] );
        }
    }

    private static int usage(){
        System.err.println("USAGE: java Geonorm <filename>");
        return -1;
    }

    private static String cleanFileName(String fileName){
        fileName = fileName.split("/")[fileName.split("/").length-1];
        fileName = fileName.replace(".ply", "");
        return fileName;
    }

    private static void printManager(Model model, String extraStr){
        System.out.println(extraStr);
        System.out.println(model.vertexNum + " vertices, " + model.faceNum + " polygons");
        System.out.println("Mean Vertex = " + "(" + model.meanVertex + ")");
        System.out.println("Bounding Box: " + model.boundingBox);
        System.out.println("Standard Deviations: " + model.standardDeviations);
    }
}
