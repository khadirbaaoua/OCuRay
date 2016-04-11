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
        ShapeSide[] res = new ShapeSide[6];
        res[0] = new ShapeSide();
        res[1] = new ShapeSide();
        res[2] = new ShapeSide();
        res[3] = new ShapeSide();
        res[4] = new ShapeSide();
        res[5] = new ShapeSide();
        List<ShapeSideElement>[] sides = new List[6];
        Point curPointMin = new Point();
        Point curPointMax = new Point();
        Point curPointMoy = new Point();
        int curFillMin = -1;
        int curFillMax = -1;
        int curFillMoy = -1;
        // Up : index 0, x = min
        // from left to right
        for (int i = shapeModel.getMiny(); i <= shapeModel.getMaxy(); i++) {
            curPointMin.move(shapeModel.getMinx(), i);
            curPointMax.move(shapeModel.getMaxx(), i);
            curPointMoy.move(shapeModel.getMinx() + shapeModel.getHeight() / 2, i);
            curFillMin = addPointToSegment(0, shape, curPointMin, sides, curFillMin, res[0]);
            curFillMax = addPointToSegment(2, shape, curPointMax, sides, curFillMax, res[2]);
            curFillMoy = addPointToSegment(4, shape, curPointMoy, sides, curFillMoy, res[4]);
        }
        curFillMin = -1;
        curFillMax = -1;
        curFillMoy = -1;
        // from up to down
        for (int i = shapeModel.getMinx(); i <= shapeModel.getMaxx(); i++) {
            curPointMin.move(i, shapeModel.getMiny());
            curPointMax.move(i, shapeModel.getMaxy());
            curPointMoy.move(i, shapeModel.getMiny() + shapeModel.getHeight() / 2);
            curFillMin = addPointToSegment(3, shape, curPointMin, sides, curFillMin, res[3]);
            curFillMax = addPointToSegment(1, shape, curPointMax, sides, curFillMax, res[1]);
            curFillMoy = addPointToSegment(5, shape, curPointMoy, sides, curFillMoy, res[5]);
        }
        res[0].setSideElts(sides[0]);
        res[1].setSideElts(sides[1]);
        res[2].setSideElts(sides[2]);
        res[3].setSideElts(sides[3]);
        res[4].setSideElts(sides[4]);
        res[5].setSideElts(sides[5]);
        shapeModel.setSideInformation(res);
    }

    // various stats for the shape
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
        shapeModel.setRatio(((double) (maxy - miny)) / ((double) (maxx - minx)));
        shapeModel.setWidth(maxy - miny);
        shapeModel.setHeight(maxx - minx);
        Point center = new Point();
        center.setLocation((maxx - ((maxx - minx) / 2.0)), (maxy - ((maxy - miny) / 2.0)));
        shapeModel.setCenter(center);
        System.out.println("Nb pixels ["+shape.size()+"] VS max/min X Y [(" +minx+","+maxx+") ("+maxy+","+miny + ")]");
        shapeModel.setOccupation(shape.size() / ((double) ((maxy - miny) * (maxx - minx))));
    }

    // binarize the shape, extract outline, vectorize and match character
    public static List<ShapeModel> analyzeShapes(List<List<Point>> shapes) {
        List<ShapeModel> res = new ArrayList<>();
        ShapeModel shapeModel;
        ShapeVector[] vecs;
        char c;
        int nbShapes = 0;
        double totalSizeDiag = 0.0, maxHeight = -1.0, totalHeight = 0.0;
        for (List<Point> shape : shapes) {
            // binarize the shape and its sides
            shapeModel = ShapeAnalysis.analyzeShape(shape);
            // vectorize shapes' sides
            vecs = ShapeUtils.vectorizeShape(shapeModel.getSideInformation());
            shapeModel.setVectors(vecs);
            // calculate the relative size overall
            totalSizeDiag += shapeModel.getDiag();
            nbShapes++;
            maxHeight = (shapeModel.getHeight() > maxHeight ? shapeModel.getHeight() : maxHeight);
            totalHeight += shapeModel.getHeight();
            res.add(shapeModel);
        }
        if (nbShapes != 0) {
            ShapeComparator.averageSize = totalSizeDiag / nbShapes;
            ShapeComparator.averageLetterHeight = totalHeight / nbShapes;
            System.out.println("Average diag size is "+ShapeComparator.averageSize);
            System.out.println("Average height size is "+ShapeComparator.averageLetterHeight);
        }
        // second run to set the relative size and compare shape with refDB
        for (ShapeModel shape : res) {
            // set the relative size
            shape.setRelativeSize(shape.getHeight() / maxHeight);
            // compare the vectors to ref db and extract matching character
            //System.out.println("Shape ratio,size = "+shape.getRatio()+ "," + shape.getRelativeSize());
            if (ShapeComparator.dbReferences != null && ShapeComparator.dbReferences.size() > 0) {
                c = ShapeComparator.compareShapeWithDb(shape);
                System.out.println("Got the character " + c);
                // and set it to the binarized shape
                shape.setC(c);
            }
        }
        return res;
    }
}
