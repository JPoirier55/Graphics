package com.graphics.main;
import com.graphics.objects.*;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.CommonOps;
import org.ejml.equation.*;
import org.ejml.ops.NormOps;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Arrays;

import static org.ejml.ops.CommonOps.*;
import static org.ejml.ops.NormOps.normF;

/**
 * Created by Jake on 10/18/2016.
 */
public class RayTracer {
    public static void main(String[] args) {
        System.out.println("helloworld");


        FileManager fileManager = new FileManager(args[0], args[1]);
        String fileName = cleanFileName(args[0]);
        Model model = new Model();
        Camera camera = new Camera();

        fileManager.loadPoints(model);
        fileManager.loadCamera(camera);
        RayHandler rayHandler = new RayHandler(model);
        PixelHandler p = new PixelHandler(rayHandler, camera);
        p.pixels();
        System.out.println(camera.toString());


        rayHandler.getMaxes();
        System.out.println(rayHandler.maxT + "   " + rayHandler.minT);
        rayHandler.printRGB();
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
}
