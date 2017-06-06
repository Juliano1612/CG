package com.w4ll3.cadeira.algebra;

/**
 * Created by manna on 30/05/17.
 */
public abstract class Transformation2D {

    /* x'   1 0 dx
    *  y' = 0 1 dy * Mobj
    *  w'   0 0 1
    */
    public Double[][] translateFigure(Double[][] mF, Double dx, Double dy) {
        Utilities utilities = new Utilities();
        Double[][] mTranslation = utilities.createMatrixTranslation(dx, dy);
        mTranslation = utilities.matrixMultiplication(mTranslation, mF);
        return mTranslation;
    }

    /* x'   1 0 dx   cosT -sinT 0   1 0 -dx
    *  y' = 0 1 dy * sinY  cosT 0 * 0 1 -dy * Mobj
    *  w'   0 0 1      0    0   1   0 0  1
    */
    public Double[][] rotateFigure(Double[][] mF, Double theta, Double dx, Double dy) {
        Utilities utilities = new Utilities();
        Double[][] mTranslationOrigin = utilities.createMatrixTranslateToOrigin(dx, dy);
        Double[][] mTranslation = utilities.createMatrixTranslation(dx, dy);
        Double[][] mRotation = utilities.createMatrixRotation(theta);

        mTranslationOrigin = utilities.matrixMultiplication(mTranslationOrigin, mF);
        mRotation = utilities.matrixMultiplication(mRotation, mTranslationOrigin);
        mTranslation = utilities.matrixMultiplication(mTranslation, mRotation);

        return mTranslation;
    }

    /* x'   1 0 dx   sx 0  0   1 0 -dx
    *  y' = 0 1 dy * 0  sy 0 * 0 1 -dy * Mobj
    *  w'   0 0 1    0  0  1   0 0  1
    */
    public Double[][] scaleFigure(Double[][] mF, Double sx, Double sy, Double dx, Double dy) {
        Utilities utilities = new Utilities();

        Double[][] mTranslationOrigin = utilities.createMatrixTranslateToOrigin(dx, dy);
        Double[][] mTranslation = utilities.createMatrixTranslation(dx, dy);
        Double[][] mScale = utilities.createMatrixScale(sx, sy);

        mTranslationOrigin = utilities.matrixMultiplication(mTranslationOrigin, mF);
        mScale = utilities.matrixMultiplication(mScale, mTranslationOrigin);
        mTranslation = utilities.matrixMultiplication(mTranslation, mScale);
        return mTranslation;
    }

}
