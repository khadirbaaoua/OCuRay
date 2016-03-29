package com.atlastic.ocuray.image.analysis;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by khadirbaaoua on 15/03/2016.
 */
public class ShapeAnalysis {
    /*
     *
     * |-------|
     * |       |
     * |       |
     * |-------|
     *
     * This represent the square area of the shape as computed in computeSizeOfShape
     *
     * 0 . Up
     * 1 . Right
     * 2 . Down
     * 3 . Left
     *
     * The shape (mostly letters), can be described as a list of used/unused range of
     * pixels for each side, which along the relativeSize (height, width) of the shape and
     * the inner shapes, will be the unique signature of the shape
     * Most letters will have their own unique signature which then will allows us to
     * identify letters
    */
    public static ShapeModel analyzeShape(List<Point> shape) {
        ShapeModel res = new ShapeModel();
        computeSizeOfShape(shape, res);
        getSideInformation(shape, res);
        return res;
    }

    public static int addPointToSegment(final int side, final List<Point> shape, final Point point,
                                        List<ShapeSideElement>[] shapeSides, final int currentFill,
                                        ShapeSide shapeSide) {
        int fillCheck;
        if (shape.contains(point)) {
            fillCheck = 1;
        } else {
            fillCheck = 0;
        }
        if (fillCheck == currentFill) {
            shapeSides[side].get(shapeSides[side].size() - 1).incLength();
        } else {
            ShapeSideElement newShape = new ShapeSideElement();
            newShape.setIsFilled(fillCheck == 1);
            newShape.setStart(new Point(point.x, point.y));
            newShape.incLength();
            if (currentFill == -1) {
                shapeSides[side] = new ArrayList<>();
            }
            shapeSides[side].add(newShape);
        }
        shapeSide.incLength();
        return fillCheck;
    }

    public static void getSideInformation(List<Point> shape, ShapeModel shapeModel) {
        // for each side,
        ShapeSide[] res = new ShapeSide[4];
        res[0] = new ShapeSide();
        res[1] = new ShapeSide();
        res[2] = new ShapeSide();
        res[3] = new ShapeSide();
        List<ShapeSideElement>[] sides = new List[4];
        Point curPointMin = new Point();
        Point curPointMax = new Point();
        int curFillMin = -1;
        int curFillMax = -1;
        // Up : index 0, x = min
        for (int i = shapeModel.getMiny(); i <= shapeModel.getMaxy(); i++) {
            curPointMin.move(shapeModel.getMinx(), i);
            curPointMax.move(shapeModel.getMaxx(), i);
            curFillMin = addPointToSegment(0, shape, curPointMin, sides, curFillMin, res[0]);
            curFillMax = addPointToSegment(2, shape, curPointMax, sides, curFillMax, res[2]);
        }
        curFillMin = -1;
        curFillMax = -1;
        for (int i = shapeModel.getMinx(); i <= shapeModel.getMaxx(); i++) {
            curPointMin.move(i, shapeModel.getMiny());
            curPointMax.move(i, shapeModel.getMaxy());
            curFillMin = addPointToSegment(3, shape, curPointMin, sides, curFillMin, res[3]);
            curFillMax = addPointToSegment(1, shape, curPointMax, sides, curFillMax, res[1]);
        }
        res[0].setSideElts(sides[0]);
        res[1].setSideElts(sides[1]);
        res[2].setSideElts(sides[2]);
        res[3].setSideElts(sides[3]);
        shapeModel.setSideInformation(res);
    }

    // square surface of shape
    public static void computeSizeOfShape(List<Point> shape, ShapeModel shapeModel) {
        if (shape.size() <= 0) {
            return;
        }
        Point firstPoint = shape.get(0);
        int minx = firstPoint.x, miny = firstPoint.y,
                maxx = firstPoint.x, maxy = firstPoint.y;
        for (Point point : shape) {
            minx = point.x < minx ? point.x : minx;
            miny = point.y < miny ? point.y : miny;
            maxx = point.x > maxx ? point.x : maxx;
            maxy = point.y > maxy ? point.y : maxy;
        }
        shapeModel.setSize((maxx - minx + 1) * (maxy - miny + 1));
        shapeModel.setMinx(minx);
        shapeModel.setMiny(miny);
        shapeModel.setMaxx(maxx);
        shapeModel.setMaxy(maxy);
        shapeModel.setDiag(ShapeMaths.computeDistanceForPoints(new Point(minx, miny), new Point(maxx, maxy)));
        shapeModel.setRatio((maxy - miny) / (maxx - minx));
        shapeModel.setWidth(maxy - miny);
        shapeModel.setHeight(maxx - minx);
        Point center = new Point();
        center.setLocation((maxx - minx) / 2, (maxy - miny) / 2);
        shapeModel.setCenter(center);
    }

    public static double computeAverageCharSize(List<ShapeModel> shapes) {
        double sum = 0.0;
        for (ShapeModel shape : shapes) {
            sum += shape.getSize();
        }
        return sum / shapes.size();
    }


    // binarize the shape, extract outline, vectorize and match character
    public static List<ShapeModel> analyzeShapes(List<List<Point>> shapes) {
        List<ShapeModel> res = new ArrayList<>();
        ShapeModel shapeModel;
        ShapeVector[] vecs;
        char c;
        int nbShapes = 0;
        double totalSizeDiag = 0.0;
        for (List<Point> shape : shapes) {
            // binarize the shape and its sides
            shapeModel = ShapeAnalysis.analyzeShape(shape);
            // vectorize shapes' sides
            vecs = ShapeUtils.vectorizeShape(shapeModel.getSideInformation());
            // compare the vectors to ref db and extract matching character
            c = ShapeComparator.compareShapeWithDb(vecs);
            // and set it to the binarized shape
            shapeModel.setC(c);
            // calculate the relative size overall
            totalSizeDiag += shapeModel.getDiag();
            nbShapes++;
            System.out.println("Got the character "+c);
        }
        if (nbShapes != 0) {
            ShapeComparator.averageSize = totalSizeDiag / nbShapes;
            System.out.println("Average diag size is "+ShapeComparator.averageSize);
        }
        return res;
    }
}
