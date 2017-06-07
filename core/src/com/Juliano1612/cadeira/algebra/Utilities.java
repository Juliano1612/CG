package com.Juliano1612.cadeira.algebra;

public class Utilities {


    /*Multiply Matrix1[n][m] * Matrix2[k][l] and return Matrix3[n][l]*/
    public Double[][] matrixMultiplication(Double[][] m1, Double[][] m2) {
        Double[][] tmp = new Double[m1.length][m2[0].length];

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                tmp[i][j] = 0d;
            }
        }

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m1[0].length; k++) {
                    tmp[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }

        return tmp;
    }

    public Double[][] createMatrixTranslation(Double x, Double y) {
        Double[][] tmp = {{1d, 0d, x}, {0d, 1d, y}, {0d, 0d, 1d}};
        return tmp;
    }

    public Double[][] createMatrixScale(Double sx, Double sy) {
        Double[][] tmp = {{sx, 0d, 0d}, {0d, sy, 0d}, {0d, 0d, 1d}};
        return tmp;
    }

    public Double[][] createMatrixRotation(Double theta) {
        Double thetaInRadian = Math.toRadians(theta);

        Double[][] tmp = {{Math.cos(thetaInRadian), (Math.sin(thetaInRadian)), 0d},
                         {Math.sin(thetaInRadian), Math.cos(thetaInRadian), 0d},
                         {0d, 0d, 1d}};
        return tmp;
    }

    public Double[][] createMatrixTranslateToOrigin(Double x, Double y) {
        Double[][] tmp = {{1d, 0d, (x*(-1))}, {0d, 1d, (y*(-1))}, {0d, 0d, 1d}};
        return tmp;
    }

    public float[] floatMatrixToArray(Float[][] m){
        float[] a = new float[2*m[0].length];
        int j = 0;
        for(int i = 0; i < m[0].length; i++){
            a[j] = m[0][i];
            a[j+1] = m[1][i];
            j+=2;
        }

        return a;
    }

}
