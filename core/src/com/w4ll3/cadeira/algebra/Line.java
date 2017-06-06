package com.w4ll3.cadeira.algebra;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Line {
    private Float[][] matrix = new Float[2][2];
    private Vector2 p1, p2;

    public Texture drawLine(Float[][] matrix){
        return null;
    }


    public Line(float x, float y, float x2, float y2) {
        matrix[0][0] = x;
        matrix[0][1] = x2;
        matrix[1][0] = y;
        matrix[1][1] = y2;
    }

    public void scale(float sx, float sy) {

        matrix[0][0] *= sx;
        matrix[0][1] *= sx;
        matrix[1][0] *= sy;
        matrix[1][1] *= sy;

    }


    public void translation(float x, float y){
        matrix[0][0] += x;
        matrix[0][1] += x;
        matrix[1][0] += y;
        matrix[1][1] += y;
    }
    
}
