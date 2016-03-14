package com.atlastic.ocuray.image.analysis;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by khadirbaaoua on 10/03/2016.
 */
public class ShapeUtils {
    public static Point computeCoordinatesForDirection(final int x, final int y, final int height, final int width,
                                                       final byte direction) {
        Point res = new Point();
        switch (direction) {
            case 1:
                res.move(x - 1, y - 1);
                break;
            case 2:
                res.move(x - 1, y);
                break;
            case 3:
                res.move(x - 1, y + 1);
                break;
            case 4:
                res.move(x, y + 1);
                break;
            case 5:
                res.move(x + 1, y + 1);
                break;
            case 6 :
                res.move(x + 1, y);
                break;
            case 7 :
                res.move(x + 1, y - 1);
                break;
            case 8 :
                res.move(x, y - 1);
                break;
        }
        res.move(res.x  >= height || res.x < 0 ? -1 : res.x,
                res.y  >= width || res.y < 0 ? -1 : res.y);
        return res;
    }

    // returns neighbouring pixels that are different than 255
    // and that were not processed
    public static List<Point> getPixelNeighbours(final int x, final int y, final short[][] pixels,
                                                 short[][] pixelsProcessed) {
        List<Point> res = new ArrayList<>();
        Point coord;
        for (byte i = 1; i <= 8; i++) {
            coord = computeCoordinatesForDirection(x, y, pixels.length, pixels[0].length, i);
            if (coord.x != -1 && coord.y != -1 && pixels[coord.x][coord.y] != 255
                    && pixelsProcessed[coord.x][coord.y] != 1) {
                res.add(coord);
            }
        }
        return res;
    }

    public static void addPixelToPatch(final Point point, List<Point> pixelPatch, short[][] processedPoints) {
        if (point != null) {
            pixelPatch.add(point);
            processedPoints[point.x][point.y] = 1;
        }
    }

    public static void addNewNeighbours(final List<Point> neighboursToAdd, List<Point> neighbours) {
        neighboursToAdd.forEach(s -> {
            if (!neighbours.contains(s)) {
                neighbours.add(s);
            }
        });
    }

    public static List<Point> getPixelPatch(final int x, final int y, final short[][] pixels,
                                            short[][] pixelsProcessed) {
        //System.out.println("Getting pixel patch for ["+x+","+y+"]");
        // Initial call
        List<Point> neighbours = getPixelNeighbours(x, y, pixels, pixelsProcessed);
        List<Point> nextNeighbours = new ArrayList<>(), pixelPatch = new ArrayList<>();
        pixelPatch.add(new Point(x, y));
        while (neighbours.size() > 0) {
            nextNeighbours.clear();
            neighbours.forEach(s -> {
                addPixelToPatch(s, pixelPatch, pixelsProcessed);
                addNewNeighbours(getPixelNeighbours(s.x, s.y, pixels, pixelsProcessed), nextNeighbours);
            });
            neighbours.clear();
            neighbours.addAll(nextNeighbours);
        }
        return pixelPatch;
    }

    public static List<List<Point>> getOutlineFromShapes(final short[][] pixels, final List<List<Point>> shapes) {
        List<List<Point>> res = new ArrayList<>();
        List<Point> curShape;
        for (List<Point> shape : shapes) {
            curShape = new ArrayList<>();
            for (Point point : shape) {
                if (isOnEdge(point.x, point.y, pixels)) {
                    curShape.add(point);
                }
            }
            if (curShape.size() > 0) {
                res.add(curShape);
            }
        }
        return res;
    }

    public static boolean isOnEdge(final int x, final int y, final short[][] pixels) {
        Point coord;
        byte nbWhite = 0;
        byte outOfBounds = 0;
        for (byte i = 1; i <= 8; i++) {
            coord = computeCoordinatesForDirection(x, y, pixels.length, pixels[0].length, i);
            if (coord.x != -1 && coord.y != -1) {
                if (pixels[coord.x][coord.y] == 255) {
                    nbWhite++;
                }
            } else {
                outOfBounds++;
            }
        }
        // angle/side or has neighbouring white pixels
        if (outOfBounds > 0 || nbWhite >= 1) {
            return true;
        }
        return false;
    }
}
