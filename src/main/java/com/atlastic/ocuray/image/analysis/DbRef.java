package com.atlastic.ocuray.image.analysis;

import java.util.List;

/**
 * Created by khadirbaaoua on 18/03/2016.
 */
public class DbRef {
    private char c;
    private double ratio;
    private ShapeVector[] sides;

    public DbRef(final char c, final double ratio, ShapeVector[] sides) {
        this.c = c;
        this.ratio = ratio;
        this.sides = sides;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public ShapeVector[] getSides() {
        return sides;
    }

    public void setSides(ShapeVector[] sides) {
        this.sides = sides;
    }
}
