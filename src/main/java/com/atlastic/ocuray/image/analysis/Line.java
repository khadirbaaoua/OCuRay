package com.atlastic.ocuray.image.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khadirbaaoua on 22/03/2016.
 */
public class Line {
    private int minx;
    private int height;
    private List<ShapeModel> letters = new ArrayList<>();
    private List<Word> words = new ArrayList<>();

    public Line(final int minx, final int height) {
        this.minx = minx;
        this.height = height;
    }

    public int getMinx() {
        return minx;
    }

    public void setMinx(final int minx) {
        this.minx = minx;
    }
    
    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public void addLetter(final ShapeModel letter) {
        this.letters.add(letter);
    }

    public void removeLetter(final ShapeModel letter) {
        this.letters.remove(letter);
    }

    public List<ShapeModel> getLetters() {
        return letters;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public List<String> toStr() {
        List<String> res = new ArrayList<>();
        for (Word word : words) {
            res.add(word.toStr());
        }
        return res;
    }
}
