package com.atlastic.ocuray.image.analysis;

import java.awt.Point;

/**
 * Created by khadirbaaoua on 15/03/2016.
 */
public class ShapeSideElement {
    private Point start;
    private int length;
    private boolean isFilled;

    public ShapeSideElement() {
        this.length = 0;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setIsFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void incLength() {
        this.length++;
    }
}
