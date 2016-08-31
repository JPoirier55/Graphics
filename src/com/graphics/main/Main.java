package com.graphics.main;

import com.graphics.objects.*;

public class Main {

    public static void main(String[] args) {
        Model m = new Model();
        m.loadPoints("assets/cube.ply");
        m.print(m.vertices);
    }
}
