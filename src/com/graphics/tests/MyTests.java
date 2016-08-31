package com.graphics.tests;

import static org.junit.Assert.assertEquals;

import com.graphics.objects.FileManager;
import com.graphics.objects.Model;
import com.graphics.objects.Point3D;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class MyTests {
    private Model testModel = new Model();

    @Test
    public void testFileManagerBlankValues() {
        FileManager tester = new FileManager("assets/test_no_values.ply");
        tester.loadPoints(this.testModel.vertices, this.testModel.plyMeta);
        ArrayList<Point3D> testList = new ArrayList<>();
        ArrayList<String> testPlyList = new ArrayList<>(Arrays.asList("ply", "format ascii 1.0", "comment created by platoply", "element vertex 8", "property float32 x", "property float32 y", "property float32 z", "element face 6", "property list uint8 int32 vertex_indices", "end_header"));
        assertEquals("No points must return blank arraylist", testList, testModel.vertices);
        assertEquals("No elements in file must return normal plyconfig list", testPlyList, testModel.plyMeta);

    }

    @Test
    public void testFileManagerBlankFile(){
        FileManager tester = new FileManager("assets/test_blank.ply");
        this.testModel.vertices = new ArrayList<>();
        tester.loadPoints(this.testModel.vertices, this.testModel.plyMeta);
        ArrayList<Point3D> testList = new ArrayList<>();
        ArrayList<String> testPlyList = new ArrayList<>();
        assertEquals("No elements in file must return blank arraylist", testList, testModel.vertices);
        assertEquals("No elements in file must return blank plyconfig list", testPlyList, testModel.plyMeta);
    }
}