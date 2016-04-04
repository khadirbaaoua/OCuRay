package com.atlastic.ocuray.text;

import com.atlastic.ocuray.image.analysis.Line;
import com.atlastic.ocuray.image.analysis.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khadirbaaoua on 22/03/2016.
 */
public class TextAnalysis {
    public static List<Double> extractAmountsFromLine(final Line line) {
        List<Double> res = new ArrayList<>();
        double tmp;
        for (Word word : line.getWords()) {

        }
        return res;
    }

    public static void matchLines(Document doc, final List<Line> lines) {
        double percentage, amount;
        String tmp;
        for (Line line : lines) {
            // try to match TVA first then TTC and finally Total
            if (line.contains("TVA")) {
                // case 1: percentage and amout on same line
                //         sub-case 1 : multiple amount on the line (before and after %)
                //         sub-case 2 : one amount
                tmp = line.extractWord("%", line);
                if (tmp != null) {
                    percentage = Double.parseDouble(tmp.substring(0, tmp.indexOf("%") - 1));

                }
            } else if (line.contains("TTC")) {

            } else if (line.contains("TOTAL")) {
                // TTC : could be total TTC/toutes taxes comprises
                // or
                // HT : Total HT/H.T./hors taxe/sous-total
            }
        }
    }


    // try to find the interesting info in the lines
    public Document doTheStuff(final List<Line> lines) {
        Document res = new Document();
        return res;
    }

}
