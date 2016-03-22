package com.atlastic.ocuray.image.analysis;

import java.util.List;

/**
 * Created by khadirbaaoua on 18/03/2016.
 */
public class ShapeSide {
    List<ShapeSideElement> sideElts;
    int length;

    public ShapeSide() {
        this.length = 0;
    }

    public ShapeSide(List<ShapeSideElement> sideElts, int length) {
        this.sideElts = sideElts;
        this.length = length;
    }

    public List<ShapeSideElement> getSideElts() {
        return sideElts;
    }

    public void setSideElts(List<ShapeSideElement> sideElts) {
        this.sideElts = sideElts;
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
