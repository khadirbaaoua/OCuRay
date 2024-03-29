package com.atlastic.ocuray.image.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khadirbaaoua on 22/03/2016.
 */
public class Line {
    private int minx;
    private double height;
    private List<ShapeModel> letters = new ArrayList<>();
    private List<Word> words = new ArrayList<>();
    private List<String> wordsStr = null;

    public Line(final int minx, final double height) {
        this.minx = minx;
        this.height = height;
    }

    public int getMinx() {
        return minx;
    }

    public void setMinx(final int minx) {
        this.minx = minx;
    }
    
    public double getHeight() {
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
        if (this.wordsStr != null) {
            return this.wordsStr;
        }
        List<String> res = new ArrayList<>();
        for (Word word : words) {
            res.add(word.toStr());
        }
        this.wordsStr = res;
        return res;
    }

    public boolean contains(final String pattern) {
        if (pattern == null || pattern.equals("")) {
            return false;
        }
        if (this.wordsStr == null) {
            this.toStr();
        }
        int i = 0;
        for (String str : wordsStr) {
            if (pattern.equalsIgnoreCase(str)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public String extractWord(final String pattern, final Line line) {
        if (pattern == null || pattern.equals("")) {
            return null;
        }
        if (this.wordsStr == null) {
            this.toStr();
        }
        String tmp;
        for (Word word : line.getWords()) {
            tmp = word.match(pattern);
            if (tmp != null) {
                return tmp;
            }
        }
        return null;
    }
}
