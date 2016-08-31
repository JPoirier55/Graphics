package com.graphics.main;

import com.graphics.objects.*;

public class Main {

    public static void main(String[] args) {
        Model m = new Model();
        FileManager fileManager = new FileManager(args[0]);
        fileManager.loadPoints(m.vertices, m.plyMeta);
        m.print(m.vertices);
        m.print_meta(m.plyMeta);
    }
}
