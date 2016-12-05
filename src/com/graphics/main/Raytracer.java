package com.graphics.main;
import com.graphics.objects.*;

/**
 * Created by Jake on 10/18/2016.
 */
public class Raytracer {

    public static void main(String[] args) {

        if(args.length == 0){
            FileManager fileManager = new FileManager();
            String scene_filename = "../assets/scenes/finalscene.txt";
            Camera camera = new Camera();
            LightHandler lightHandler = new LightHandler();
            ObjectHandler objectHandler = new ObjectHandler();
            System.out.println("Loading scene....");
            fileManager.loadSceneFile(scene_filename, camera, lightHandler, objectHandler);
            System.out.println("Starting render....");
            RayHandler rayHandler = new RayHandler(objectHandler, camera, lightHandler);
            RayHandler2 rayHandler2 = new RayHandler2(objectHandler, camera, lightHandler);

            PixelHandler p = new PixelHandler(rayHandler, rayHandler2, camera);
            p.pixels();
            fileManager.writePPM(p, "../assets/finalscene.ppm", camera);

            System.out.println("Finished render.");
            System.out.println("Output file: finalscene.ppm");
        }else {
            FileManager fileManager = new FileManager();
            String fileName = cleanFileName(args[0]);
            String scene_filename = args[0];
            Camera camera = new Camera();
            LightHandler lightHandler = new LightHandler();
            ObjectHandler objectHandler = new ObjectHandler();
            System.out.println("Loading scene....");
            fileManager.loadSceneFile(scene_filename, camera, lightHandler, objectHandler);
            System.out.println("Starting render....");
            RayHandler rayHandler = new RayHandler(objectHandler, camera, lightHandler);
            RayHandler2 rayHandler2 = new RayHandler2(objectHandler, camera, lightHandler);

            PixelHandler p = new PixelHandler(rayHandler, rayHandler2, camera);
            p.pixels();
            fileManager.writePPM(p, args[1], camera);

            System.out.println("Finished render.");
            System.out.println("Output file: " + args[1]);
        }
    }

    private static int usage(){
        System.err.println("USAGE: java Raytracer <input Scene File>  <output file name>");
        return -1;
    }

    private static String cleanFileName(String fileName){
        fileName = fileName.split("/")[fileName.split("/").length-1];
        fileName = fileName.replace(".ply", "");
        return fileName;
    }
}
