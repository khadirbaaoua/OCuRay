package com.atlastic.ocuray.image.analysis;


/**
 * Created by khadirbaaoua on 17/03/2016.
 */
public class ShapeVector {
    private int size;
    private double[] coord;

    public ShapeVector(String str) {
        //System.out.println("New shape vector with string : "+str);
        String[] coords = str.split(",");
        if (coords != null && coords.length > 0) {
            this.size = coords.length;
            this.coord = new double[coords.length];
            int i = 0;
            for (String doubleStr : coords) {
                //System.out.println("Trying to parse : "+doubleStr);
                this.coord[i++] = Double.parseDouble(doubleStr);
            }
        }
    }

    public ShapeVector(final int size) {
        this.size = size;
        this.coord = new double[size];
    }

    public void setCoord(final int index, final double value) {
        this.coord[index] = value;
    }

    public double getAt(final int index) {
        return this.coord[index];
    }

    public double[] getAll() {
        return this.coord;
    }

    public void setAt(final int index, final double value) {
        this.coord[index] = value;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
