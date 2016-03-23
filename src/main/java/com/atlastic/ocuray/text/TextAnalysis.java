package com.atlastic.ocuray.text;

import com.atlastic.ocuray.Constants;
import com.atlastic.ocuray.image.analysis.Line;

import java.util.List;

/**
 * Created by khadirbaaoua on 22/03/2016.
 */
public class TextAnalysis {
    //

    // extract amount from line
    public static double extractAmount(final Line line) {
        double res = -1;
        for (String str : line.toStr()) {
            for
        }
        return res;
    }

    // extract amount from line
    public static double extractPercentage(final Line line) {
        double res = -1;
        for (String str : line.toStr()) {

        }
        return res;
    }


    // get the TTC total from the list of string
    public static List<PercentTotal> getTTCTotal(final List<Line> lines) {
        List<PercentTotal> res = null;
        PercentTotal current;
        int pos;
        for (Line line : lines) {
            pos = line.contains(Constants.TTC_PATTERN);
            if (pos != -1) {
                current = new PercentTotal();
                current.setAmount(extractAmount(line));
                current.setAmount(extractPercentage(line));
            }
        }
        return res;
    }

    public static void getHTTotal(final List<Line> lines) {
        // there could multiple HT total
    }


}
