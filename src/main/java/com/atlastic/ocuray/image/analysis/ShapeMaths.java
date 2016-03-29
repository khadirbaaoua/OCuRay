package com.atlastic.ocuray.image.analysis;

import java.awt.*;

/**
 * Created by khadirbaaoua on 28/03/2016.
 */
public class ShapeMaths {
    public static double computeDistanceForPoints(final Point p1, final Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }
}
