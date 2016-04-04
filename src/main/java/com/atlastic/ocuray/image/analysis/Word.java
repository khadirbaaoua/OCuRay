package com.atlastic.ocuray.image.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khadirbaaoua on 22/03/2016.
 */
public class Word {
    private List<ShapeModel> letters;
    private boolean isCompound = false;
    private String str = null;

    public Word(final ShapeModel ... args) {
        letters = new ArrayList<>();
        for (ShapeModel letter : args) {
            letters.add(letter);
        }
    }

    public void addLetter(final ShapeModel letter) {
        if (!letter.isHollow()) {
            letters.add(letter);
        }
    }

    public String toStr() {
        if (this.str != null) {
            return this.str;
        }
        StringBuffer str = new StringBuffer();
        for (ShapeModel letter : letters) {
            str.append(letter.getC());
        }
        this.str = str.toString();
        return str.toString();
    }

    public String match(final String pattern) {
        if (pattern == null || pattern.equals("")) {
            return null;
        }
        if (this.str == null) {
            this.toStr();
        }
        if (this.str.contains(pattern)) {
            return this.str;
        }
        return null;
    }
}
