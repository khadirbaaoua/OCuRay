package com.atlastic.ocuray.image.analysis;

import java.awt.*;

/**
 * Created by khadirbaaoua on 28/03/2016.
 */
public class ShapeMaths {
    public static double computeDistanceForPoints(final Point p1, final Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public static boolean matchDoublesSigns(final double[] d1, final double[] d2) {
        for (int i = 0; i < d1.length; i++) {
            if (!areSameSigns(d1[i], d2[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean areSameSigns(final double d1, final double d2) {
        return (d1 > 0 && d2 > 0) || (d1 < 0 && d2 < 0) || (d1 == 0 && d2 == 0);
    }
}
