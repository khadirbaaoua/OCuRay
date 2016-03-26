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


    // group compounds by char (compounds are already ordered in the ref db file
    public static List<List<DbRef>> groupCompounds(final List<DbRef> compounds) {
        List<List<DbRef>> res = new ArrayList<>();
        List<DbRef> currentCompound = null;
        char currentC = 't';
        for (DbRef dbRef : compounds) {
            if (currentC != dbRef.getC()) {
                if (currentCompound != null) {
                    res.add(currentCompound);
                }
                currentCompound = new ArrayList<>();
            } else {
                currentCompound.add(dbRef);
            }
        }
        return res;
    }

    // extract compound char ref from db
    public static List<DbRef> getRefCompounds(final List<DbRef> dbRefs) {
        List<DbRef> res = new ArrayList<>();
        for (DbRef dbRef : dbRefs) {
            if (dbRef.isMultipartRef()) {
                res.add(dbRef);
            }
        }
        return res;
    }

    // return yes if the character could be part of a compound in the ref db
    public static boolean isKeyTocompoundCharacter(final char c, final List<DbRef> dbRefs) {
        for (DbRef dbRef : dbRefs) {
            if (!dbRef.isMultipartRef() && c == dbRef.getC()) {
                return true;
            }
        }
        return false;
    }

    // compute distance between each found compounds and return true if each character are within
    // the distance of the size of an A
    public static boolean isCompoundInTheSameArea(final List<ShapeModel> letters) {

        return false;
    }

    // try to match a compound with a list of letter (n->n matching)
    public static boolean matchSingleCompoundWithLine(final List<DbRef> compound, final List<ShapeModel> letters) {
        int[] matchedLetters = new int[compound.size()];
        for (ShapeModel letter : letters) {
            for (DbRef compoundLetter : compound) {
                if (letter.getC() == compoundLetter.getC() && matchedLetters[compoundLetter.getCounter()] != 1) {
                    matchedLetters[compoundLetter.getCounter()] = 1;
                }
            }
        }
        for (int i : matchedLetters) {
            if (i != 1) {
                return false;
            }
        }
        return true;
    }

    // try to match a any of the compound within the line
    public static boolean matchCompounsdWithLine(final List<List<DbRef>> compounds, final Line line) {
        List<ShapeModel> letters = line.getLetters();
        List<ShapeModel> compoundMatch = null;
        boolean isMatch;
        for (List<DbRef> compound : compounds) {
            if (matchSingleCompoundWithLine(compound, letters)) {
                return true;
            }
        }
        return false;
    }

    // utility to merge shapes that share the same space but are not linked
    // i.e.  : ; % ! ? = ¨ "
    public static void regroupSplittedShapes(Line line) {
        // in the line, we try to see if there any characters that match compounds in the ref db
        // if so, we try to see if any set of matched characters are in the line and in the same
        // vicinity

    }


    // wrapper to regroup all letters for all lines
    public static void regroupAllLetters(List<Line> lines) {
        double spacing;
        for (Line line : lines) {
            spacing = getMaximumSpacingForLine(line);
            regroupLettersByWords(line, spacing);
        }
    }

    // calculate the maximum spacing between letters
    public static double getMaximumSpacingForLine(final Line line) {
        double res = -1, tmp;
        List<ShapeModel> letters = line.getLetters();
        for (int i = 0; i < letters.size() - 2; i++) {
            tmp = getSpacingBetweenLetters(letters.get(i), letters.get(i + 1));
            res = ((res == -1 || tmp > res) ? tmp : res);
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
