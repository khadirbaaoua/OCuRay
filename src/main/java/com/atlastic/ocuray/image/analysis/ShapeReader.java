package com.atlastic.ocuray.image.analysis;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by khadirbaaoua on 08/03/2016.
 */
public class ShapeReader {

    public static List<List<Point>> readShapes(final short[][] pixels) {
        List<List<Point>> res = new ArrayList<>();
        // by convention, 1 pixel is processed, 0 pixel has not been processed
        short[][] pixelsProcessed = new short[pixels.length][pixels[0].length];
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                if (pixels[i][j] == 255) {
                    continue;
                }
                if (pixelsProcessed[i][j] != 1) {
                    //System.out.println("Processing coordinates : ["+i+","+j+"]");
                    res.add(ShapeUtils.getPixelPatch(i, j, pixels, pixelsProcessed));
                }
            }
        }
        return res;
    }

    /*
    *
    *                 Directions are defined by :
    *
    *                    + is the pixel
    *
    *                  1 2 3
    *                  8 + 4
    *                  7 6 5
    *
    *
    *
     */

}
