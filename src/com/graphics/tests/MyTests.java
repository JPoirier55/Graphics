package com.graphics.tests;

import static org.junit.Assert.assertEquals;

import com.graphics.objects.Face;
import com.graphics.objects.FileManager;
import com.graphics.objects.Model;
import com.graphics.objects.Point3D;
import org.junit.Test;

import java.util.ArrayList;

public class MyTests {
    private Model testModel = new Model();
    FileManager tester;

    @Test
    public void testFileManagerBlankValues() {
        tester = new FileManager("assets/test_no_values.ply");
        tester.loadPoints(testModel);
        ArrayList<Point3D> testList = new ArrayList<>();
        ArrayList<Face> testFaces = new ArrayList<>();
        assertEquals("No points must return blank arraylist", testList, testModel.vertices);
        assertEquals("No elements in file must return blank faces list", testFaces, testModel.faces);

    }

    @Test
    public void testFileManagerBlankFile(){
        tester = new FileManager("assets/test_blank.ply");
        this.testModel.vertices = new ArrayList<>();
        tester.loadPoints(testModel);
        ArrayList<Point3D> testList = new ArrayList<>();
        ArrayList<Face> testFaces = new ArrayList<>();
        assertEquals("No elements in file must return blank arraylist", testList, testModel.vertices);
        assertEquals("No elements in file must return blank faces list", testFaces, testModel.faces);
    }
}