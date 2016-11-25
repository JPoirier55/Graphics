package com.graphics.main;
import com.graphics.objects.*;

/**
 * Created by Jake on 10/18/2016.
 */
public class Raytracer {

    public static void main(String[] args) {

        if(args.length < 3){
            System.exit(usage());
        }
        FileManager fileManager = new FileManager();
        String fileName = cleanFileName(args[0]);
        Camera camera = new Camera();
        LightHandler lightHandler = new LightHandler();
        ObjectHandler objectHandler = new ObjectHandler();


//        fileManager.loadPoints(model);
//        fileManager.loadObjFile(camera, lightHandler, objectHandler);
//        RayHandler rayHandler = new RayHandler(model, camera);
//        PixelHandler p = new PixelHandler(rayHandler, camera);
//        System.out.println("Starting depth trace....");
//        p.pixels();
//        p.getMaxes();
//        p.pixelsetter();
//        fileManager.writePPM(p, args[2], camera);
//        System.out.println("Finished depth trace.");
//        System.out.println("Output file: "+args[2]);
    }

    private static int usage(){
        System.err.println("USAGE: java RayTracer <input PLY file> <input Camera File> <output file name");
        return -1;
    }

    private static String cleanFileName(String fileName){
        fileName = fileName.split("/")[fileName.split("/").length-1];
        fileName = fileName.replace(".ply", "");
        return fileName;
    }
}
