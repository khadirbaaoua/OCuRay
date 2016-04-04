package com.atlastic.ocuray.text;

import com.atlastic.ocuray.image.analysis.Line;
import com.atlastic.ocuray.image.analysis.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khadirbaaoua on 22/03/2016.
 */
public class TextAnalysis {
    // extract all amounts in the line
    public static List<Double> extractAmountsFromLine(final Line line) {
        List<Double> res = new ArrayList<>();
        double tmp;
        for (Word word : line.getWords()) {
            try {
                if (word.match("%") !=  null) {
                    continue;
                }
                tmp = Double.parseDouble(word.toStr().replace(",", "."));
                res.add(tmp);
            } catch (NumberFormatException e) {
                System.out.println("[" + word.toStr() + "] is not a number");
            }
        }
        return res;
    }

    // do the actual matching
    public static void matchLines(Document doc, final List<Line> lines) {
        double percentage, amount;
        String tmp;
        List<Double> listTmp;
        for (Line line : lines) {
            // try to match TVA first then TTC and finally Total
            if (line.contains("TVA")) {
                // case 1: percentage and amout on same line
                //         sub-case 1 : multiple amount on the line (before and after %)
                //         sub-case 2 : one amount
                tmp = line.extractWord("%", line);
                if (tmp != null) {
                    System.out.println("Trying to extract percentage from ["+tmp+"]");
                    tmp = tmp.replace(",", ".");
                    percentage = Double.parseDouble(tmp.substring(0, tmp.indexOf("%")));
                    listTmp = extractAmountsFromLine(line);
                    if (listTmp.size() == 0) {
                        // errm, no amounts seriously ?
                        System.out.println("No amounts dude !");
                    }
                    else if (listTmp.size() == 1) {
                        doc.getTvaList().put(percentage, listTmp.get(0));
                    } else if (listTmp.size() == 2) {
                        System.out.println("2 amounts dude !");
                    } else {
                        // shit, more than 2 amounts ? no can do man
                        System.out.println("More than 2 amounts dude !");
                    }
                }
            } else if (line.contains("TTC")) {

            } else if (line.contains("TOTAL")) {
                // TTC : could be total TTC/toutes taxes comprises
                // or
                // HT : Total HT/H.T./hors taxe/sous-total
            } else {
                // try to match other stuff (date, category, ...)
            }
        }
    }


    // try to find interesting info in the lines
    public static Document doTheStuff(final List<Line> lines) {
        Document res = new Document();
        matchLines(res, lines);
        return res;
    }

}
