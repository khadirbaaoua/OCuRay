package com.atlastic.ocuray.image.filter;

/**
 * Created by khadirbaaoua on 10/01/2016.
 */
public class NoiseFilter implements AbstractFilter {
    // ALGO : delete all pixels that have no neighbours (i.e white pixels)
    public static void doFilter(short[][] pixels) {
        for (int i = 0; i < pixels.length; i ++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (pixels[i][j] == 255) {
                    continue ;
                }
                pixels[i][j] = (hasAnyNeighbour(i, j, pixels) ? pixels[i][j] : 255);
            }
        }
    }

    public static boolean hasAnyNeighbour(final int x, final int y, short[][] pixels) {
        boolean res = false;
        // check the eight neighbours
        // first upper part
        if (x != 0) {
            // checking upper part
            res |= (pixels[x - 1][y] != 255);
            // previous pixel
            if (y != 0) {
                res |= (pixels[x - 1][y - 1] != 255);
            }
            // next pixel
            if (y != pixels[0].length - 1) {
                res |= (pixels[x - 1][y + 1] != 255);
            }
        }
        // then middle part
        if (y != 0) {
            res |= (pixels[x][y - 1] != 255);
        }
        if (y != pixels[0].length - 1) {
            res |= (pixels[x][y + 1] != 255);
        }
        // finally lower part
        if (x != pixels.length - 1) {
            // checking lower part
            res |= (pixels[x + 1][y] != 255);
            // previous pixel
            if (y != 0) {
                res |= (pixels[x + 1][y - 1] != 255);
            }
            // next pixel
            if (y != pixels[0].length - 1) {
                res |= (pixels[x + 1][y + 1] != 255);
            }
        }
        return res;
    }
}
