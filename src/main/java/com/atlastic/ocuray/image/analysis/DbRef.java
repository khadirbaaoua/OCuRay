package com.atlastic.ocuray.image.analysis;

import java.util.List;

/**
 * Created by khadirbaaoua on 18/03/2016.
 */
public class DbRef {
    private char c;
    private double ratio;
    private double relativeSize;
    private ShapeVector[] sides;
    private DbRef ref = null;
    private int counter;
    private int total;
    private int lineNumber;
    private double occupation;

    public DbRef(final char c, final double ratio, final double relativeSize, final ShapeVector[] sides,
                 final int lineNumber, final double occupation) {
        this.c = c;
        this.ratio = ratio;
        this.sides = sides;
        this.relativeSize = relativeSize;
        this.lineNumber = lineNumber;
        this.occupation = occupation;
    }

    public DbRef(char c, DbRef ref, final int counter, final int total, final int lineNumber, final double occupation) {
        this.c = c;
        this.ref = ref;
        this.counter = counter;
        this.total = total;
        this.lineNumber = lineNumber;
        this.occupation = occupation;
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

    public boolean isMultipartRef() {
        return ref != null;
    }

    public DbRef getRef() {
        return ref;
    }

    public int getCounter() {
        return counter;
    }

    public int getTotal() {
        return total;
    }

    public double getRelativeSize() {
        return relativeSize;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public double getOccupation() {
        return occupation;
    }

    public void setOccupation(double occupation) {
        this.occupation = occupation;
    }
}
