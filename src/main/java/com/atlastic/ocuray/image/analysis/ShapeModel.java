package com.atlastic.ocuray.image.analysis;

import com.atlastic.ocuray.Constants;

import java.awt.*;
import java.util.List;

/**
 * Created by khadirbaaoua on 15/03/2016.
 */
public class ShapeModel implements Comparable<ShapeModel> {
    private int size;
    private double ratio;
    private double relativeSize;
    private int minx;
    private int miny;
    private int maxx;
    private int maxy;
    private int height;
    private int width;
    private double diag;
    private Point center;
    private Line line;
    private char c;
    ShapeSide[] sideInformation;
    ShapeVector[] vectors;
    private boolean isHollow = false;

    public ShapeModel() {
        this.size = 0;
        this.ratio = 1;
        this.minx = 0;
        this.miny = 0;
        this.maxx = 0;
        this.maxy = 0;
        this.sideInformation = null;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public int getMinx() {
        return minx;
    }

    public void setMinx(int minx) {
        this.minx = minx;
    }

    public int getMiny() {
        return miny;
    }

    public void setMiny(int miny) {
        this.miny = miny;
    }

    public int getMaxx() {
        return maxx;
    }

    public void setMaxx(int maxx) {
        this.maxx = maxx;
    }

    public int getMaxy() {
        return maxy;
    }

    public void setMaxy(int maxy) {
        this.maxy = maxy;
    }

    public ShapeSide[] getSideInformation() {
        return sideInformation;
    }

    public void setSideInformation(ShapeSide[] sideInformation) {
        this.sideInformation = sideInformation;
    }

    // histogram for each side
    public ShapeVector[] getVector() {
        ShapeVector res[] = new ShapeVector[4];

        return res;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public double getRelativeSize() {
        return relativeSize;
    }

    public void setRelativeSize(double relativeSize) {
        this.relativeSize = relativeSize;
    }

    public boolean isHollow() {
        return isHollow;
    }

    public void setIsHollow(boolean isHollow) {
        this.isHollow = isHollow;
    }

    public double getDiag() {
        return diag;
    }

    public void setDiag(double diag) {
        this.diag = diag;
    }

    public ShapeVector[] getVectors() {
        return vectors;
    }

    public void setVectors(ShapeVector[] vectors) {
        this.vectors = vectors;
    }

    @Override
    public int compareTo(ShapeModel o) {
        if (this.getMiny() < o.getMiny()) {
            return -1;
        } else if (this.getMiny() > o.getMiny()) {
            return 1;
        } else {
            return 0;
        }
    }
}
