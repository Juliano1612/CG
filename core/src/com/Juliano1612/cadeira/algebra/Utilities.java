package com.Juliano1612.cadeira.algebra;

public class Utilities {


    /*Multiply Matrix1[n][m] * Matrix2[k][l] and return Matrix3[n][l]*/
    public Float[][] matrixMultiplication(Float[][] m1, Float[][] m2) {
        Float[][] tmp = new Float[m1.length][m2[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                tmp[i][j] = 0f;
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

    public Float[][] createMatrixTranslation(Float x, Float y) {
        Float[][] tmp = {{1f, 0f, x}, {0f, 1f, y}, {0f, 0f, 1f}};
        return tmp;
    }

    public Float[][] createMatrixScale(Float sx, Float sy) {
        Float[][] tmp = {{sx, 0f, 0f}, {0f, sy, 0f}, {0f, 0f, 1f}};
        return tmp;
    }

    public Float[][] createMatrixRotation(Float theta) {
        Float thetaInRadian = (float)Math.toRadians(theta);

        Float[][] tmp = {{(float)Math.cos(thetaInRadian), -((float)Math.sin(thetaInRadian)), 0f},
                         {(float)Math.sin(thetaInRadian), (float)Math.cos(thetaInRadian), 0f},
                         {0f, 0f, 1f}};
        return tmp;
    }

    public Float[][] createMatrixTranslateToOrigin(Float x, Float y) {
        Float[][] tmp = {{1f, 0f, (x*(-1f))}, {0f, 1f, (y*(-1))}, {0f, 0f, 1f}};
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

    public void printMatrix(Float[][] m){
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[0].length; j++){
                System.out.printf(" "+m[i][j]);
            }
            System.out.println("");
        }
    }

}
