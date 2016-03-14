package com.atlastic.ocuray.image.filter;

import java.awt.image.BufferedImage;

/**
 * Created by khadirbaaoua on 10/01/2016.
 */
public class ContrastFilter implements AbstractFilter{
    private static final short bypass = 180;
    // contrast filter => to B/W
    public static void doFilter(short[][] pixels) {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                pixels[i][j] = (short) (pixels[i][j] > bypass ? 255 : 0);
            }
        }
    }
}
