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


        FileManager fileManager = new FileManager(args[0], args[1]);
        String fileName = cleanFileName(args[0]);
        Model model = new Model();
        Camera camera = new Camera();

        fileManager.loadPoints(model);
        fileManager.loadCamera(camera);
//        fileManager.writePoints(model, "C:\\Users\\Jake\\git3\\Graphics\\assets\\tester.ply");
        RayHandler rayHandler = new RayHandler(model, camera);
        System.out.println(camera.toString());
        PixelHandler p = new PixelHandler(rayHandler, camera);
        p.pixels();
        p.getMaxes();
        p.pixelsetter();


        System.out.println(p.maxT + "   " + p.minT);
        p.printRGB();
        fileManager.writePPM(p, args[2], camera);
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
