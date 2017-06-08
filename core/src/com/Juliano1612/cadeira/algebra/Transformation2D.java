package com.Juliano1612.cadeira.algebra;

public class Transformation2D {

    /* x'   1 0 dx
    *  y' = 0 1 dy * Mobj
    *  w'   0 0 1
    */
    public Float[][] translateFigure(Float[][] mF, Float dx, Float dy) {
        Utilities utilities = new Utilities();
        Float[][] mTranslationOrigin = utilities.createMatrixTranslateToOrigin(mF[0][0], mF[1][0]);
        Float[][] mTranslation = utilities.createMatrixTranslation(dx, dy);
        mTranslationOrigin = utilities.matrixMultiplication(mTranslationOrigin, mF);
        mTranslation = utilities.matrixMultiplication(mTranslation, mTranslationOrigin);
        new Utilities().printMatrix(mTranslation);
        return mTranslation;
    }

    /* x'   1 0 dx   cosT -sinT 0   1 0 -dx
    *  y' = 0 1 dy * sinY  cosT 0 * 0 1 -dy * Mobj
    *  w'   0 0 1      0    0   1   0 0  1
    */
    public Float[][] rotateFigure(Float[][] mF, Float theta) {
        Utilities utilities = new Utilities();
        Float[][] mTranslationOrigin = utilities.createMatrixTranslateToOrigin(mF[0][0], mF[1][0]);
        Float[][] mTranslation = utilities.createMatrixTranslation(mF[0][0], mF[1][0]);
        Float[][] mRotation = utilities.createMatrixRotation(theta);

        mTranslationOrigin = utilities.matrixMultiplication(mTranslationOrigin, mF);
        mRotation = utilities.matrixMultiplication(mRotation, mTranslationOrigin);
        mTranslation = utilities.matrixMultiplication(mTranslation, mRotation);

        return mTranslation;
    }

    /* x'   1 0 dx   sx 0  0   1 0 -dx
    *  y' = 0 1 dy * 0  sy 0 * 0 1 -dy * Mobj
    *  w'   0 0 1    0  0  1   0 0  1
    */
    public Float[][] scaleFigure(Float[][] mF, Float sx, Float sy) {
        Utilities utilities = new Utilities();

        Float[][] mTranslationOrigin = utilities.createMatrixTranslateToOrigin(mF[0][0], mF[1][0]);
        Float[][] mTranslation = utilities.createMatrixTranslation(mF[0][0], mF[1][0]);
        Float[][] mScale = utilities.createMatrixScale(sx, sy);

        mTranslationOrigin = utilities.matrixMultiplication(mTranslationOrigin, mF);
        mScale = utilities.matrixMultiplication(mScale, mTranslationOrigin);
        mTranslation = utilities.matrixMultiplication(mTranslation, mScale);
        return mTranslation;
    }

}
