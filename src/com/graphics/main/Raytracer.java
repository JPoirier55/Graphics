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
        FileManager fileManager = new FileManager(args[0], args[1]);
        String fileName = cleanFileName(args[0]);
        Model model = new Model();
        Camera camera = new Camera();

        fileManager.loadPoints(model);
        fileManager.loadCamera(camera);
        RayHandler rayHandler = new RayHandler(model, camera);
        PixelHandler p = new PixelHandler(rayHandler, camera);
        System.out.println("Starting depth trace....");
        p.pixels();
        p.getMaxes();
        p.pixelsetter();
        fileManager.writePPM(p, args[2], camera);
        System.out.println("Finished depth trace.");
        System.out.println("Output file: "+args[2]);
    }

    private static int usage(){
        System.err.println("USAGE: java Raytracer <input Camera File> <input PLY file> <output file name");
        return -1;
    }

    private static String cleanFileName(String fileName){
        fileName = fileName.split("/")[fileName.split("/").length-1];
        fileName = fileName.replace(".ply", "");
        return fileName;
    }
}
