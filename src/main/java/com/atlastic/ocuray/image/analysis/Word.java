package com.atlastic.ocuray.image.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khadirbaaoua on 22/03/2016.
 */
public class Word {
    private List<ShapeModel> letters;
    private boolean isCompound = false;

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

    public void removeLetter(final ShapeModel letter) {
        letters.remove(letter);
    }

    public String toStr() {
        StringBuffer str = new StringBuffer();
        for (ShapeModel letter : letters) {
            str.append(letter.getC());
        }
        return str.toString();
    }

    public void markAsCompound() {
        this.isCompound = true;
    }
}
