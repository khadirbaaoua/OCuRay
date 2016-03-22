package com.atlastic.ocuray.image.analysis;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by khadirbaaoua on 22/03/2016.
 */
public class ShapeSemantics {
    // try to see if a shape fits into any of the lines
    public static Line getLineFromShape(final ShapeModel shape, final List<Line> lines) {
        Point center;
        for (Line line : lines) {
            center = shape.getCenter();
            if (center.x >= line.getMinx() && (center.x <= (line.getMinx() + line.getHeight()))) {
                return line;
            }
        }
        return null;
    }

    // regroup shapes by lines
    public static List<Line> regroupShapesByLine(final List<ShapeModel> shapes) {
        // we try to see if it fits any of the line existing (i.e. center between line.minx and line.minx + lineHeight
        // if not we create a new line and assign it
        List<Line> lines = new ArrayList<>();
        Line match;
        for (ShapeModel shape : shapes) {
            match = getLineFromShape(shape, lines);
            if (match != null) {
                shape.setLine(match);
            } else {
                match = new Line(shape.getMinx(), shape.getHeight());
            }
            match.addLetter(shape);
        }
        return lines;
    }

    // get the minimum space between two letters
    public static double getSpacingBetweenLetters(final ShapeModel letter1, final ShapeModel letter2) {
        double res = -1.0;
        Point center1 = letter1.getCenter();
        Point center2 = letter2.getCenter();
        if (center1.getY() > center2.getY()) {
            res = letter1.getMiny() - letter2.getMaxy();
        } else {
            res = letter2.getMiny() - letter1.getMaxy();
        }
        return res;
    }

    // for each line, regroup shape by words based on the spacing
    public static void regroupLettersByWords(Line line, final double spacing) {
        List<Word> words = new ArrayList<>();
        List<ShapeModel> letters = line.getLetters();
        Word current = new Word();
        double currentSpacing;
        for (int i = 0; i < letters.size() - 1; i++) {
             if (i == 0 || i == letters.size() - 1) {
                 current.addLetter(letters.get(i));
             } else {
                 currentSpacing = getSpacingBetweenLetters(letters.get(i - 1), letters.get(i));
                 if (currentSpacing <= spacing) {
                     current.addLetter(letters.get(i));
                 } else {
                     current = new Word(letters.get(i));
                 }
             }
        }
        line.setWords(words);
    }

    // wrapper to regroup all letters for all lines
    public static void regroupAllLetters(List<Line> lines) {
        double spacing;
        for (Line line : lines) {
            spacing = getMinimumSpacingForLine(line);
            regroupLettersByWords(line, spacing);
        }
    }

    // calculate the minimum spacing between letters
    public static double getMinimumSpacingForLine(final Line line) {
        double res = -1, tmp;
        List<ShapeModel> letters = line.getLetters();
        for (int i = 0; i < letters.size() - 2; i++) {
            tmp = getSpacingBetweenLetters(letters.get(i), letters.get(i + 1));
            res = ((res == -1 || tmp < res) ? tmp : res);
        }
        return res;
    }

    // do all the work expected
    public static List<Line> getSemanticsOutOfShapes(final List<ShapeModel> shapes) {
        // regroup shapes by line
        List<Line> lines = regroupShapesByLine(shapes);
        // and then regroup the letters for each line
        regroupAllLetters(lines);
        return lines;
    }

}
