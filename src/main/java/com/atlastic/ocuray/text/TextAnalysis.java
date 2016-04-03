package com.atlastic.ocuray.text;

import com.atlastic.ocuray.image.analysis.Line;

import java.util.List;

/**
 * Created by khadirbaaoua on 22/03/2016.
 */
public class TextAnalysis {

    public static void matchLines(Document doc, final List<Line> lines) {
        for (Line line : lines) {
            // try to match TVA first
            if (line.toStr().contains("TVA") || line.toStr().contains("tva")) {
                
            }
        }
    }


    // try to find the interesting info in the lines
    public Document doTheStuff(final List<Line> lines) {
        Document res = new Document();
        return res;
    }

}
