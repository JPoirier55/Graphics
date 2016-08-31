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
    public ArrayList<String> plyMeta = new ArrayList<>();

    public void print(ArrayList arrayList){
        for(int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i).toString());
        }
    }

    public void print_meta(ArrayList arrayList){
        for(int i = 0; i < arrayList.size(); i++) {
            System.out.print(arrayList.get(i).toString());
        }
    }
}
