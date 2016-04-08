package com.atlastic.ocuray;

import com.atlastic.ocuray.image.analysis.*;
import com.atlastic.ocuray.image.filter.ContrastFilter;
import com.atlastic.ocuray.image.filter.NoiseFilter;
import com.atlastic.ocuray.image.tool.FileHelper;
import com.atlastic.ocuray.image.tool.ImageHelper;
import com.atlastic.ocuray.text.Document;
import com.atlastic.ocuray.text.TextAnalysis;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainRef {
    static char[] capitals = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R'
    ,'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    static char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'
    ,'s', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    static char[] lettersref = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', '.', 'i', '.', 'j', 'k', 'l', 'm', 'n', 'o'
            , 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    static char[] numbers = {'1', '2', '3', '4', '5', '6', '7', '8' , '9', '0'};
    static char[] symbols = {'('};
    static char[] symbolsref;

    public static void createDirectory(final String path, final String font) {
        File f = new File(path + font);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    public static void generateImageLetters(final String path, final String font) throws IOException {
        ImageHelper.createImageWithText(capitals, path + "capitals-generated.png", font);
        ImageHelper.createImageWithText(letters, path + "letters-generated.png", font);
        ImageHelper.createImageWithText(numbers, path + "numbers-generated.png", font);
    }

    public static void getFonts() {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts(); // Get the fonts
        for (Font f : fonts) {
            System.out.println(f.getFontName());
        }
    }

    public static void main(String[] args) throws IOException{
	    String[] refs = {"capitals-generated.png", "letters-generated.png", "numbers-generated.png"/*, "signs.png"*/};
        String[] fonts = {"Arial-black", "Verdana", "Courier", "Helvetica", "TrebuchetMS", "Times-Roman", "Tahoma",
        "Lucida Sans Regular", "Georgia", "ComicSansMS", "Avenir-Black", "Serif"};
        String path = "ref/";
        String currentPath;
        int i;
        getFonts();
        for (String font : fonts) {
            createDirectory(path, font);
            generateImageLetters(path + font + "/", font);
            i = 0;
            for (String ref : refs) {
                currentPath = path + font + "/" + ref;
                System.out.println("Trying to read img " + currentPath);
                BufferedImage img = ImageHelper.loadImageFromPath(currentPath);
                short[][] pixels = ImageHelper.convertTo2DAndGrayscaleUsingRGB(img);
                ContrastFilter.doFilter(pixels);
                NoiseFilter.doFilter(pixels);
                System.out.println("Image filtering done, reading shapes");
                List<List<Point>> shapes = ShapeReader.readShapes(pixels);
                System.out.println("Shape reading done, " + shapes.size() + " shapes red");
                System.out.println("Converting shapes to outline");
                List<List<Point>> shapesOutline = ShapeUtils.getOutlineFromShapes(pixels, shapes);
                System.out.println("Shapes successfully converted to outline, writing to file");
                FileHelper.writeShapesToImage(pixels.length, pixels[0].length, currentPath + "-shapes-outline.png", shapesOutline,
                        pixels);
                System.out.println("Analyzing shape outline");
                List<ShapeModel> letters = ShapeAnalysis.analyzeShapes(shapes);
                // write info in file
                setLetters(letters, matchCharRef(i));
                FileHelper.writeShapeModelToFile(currentPath + "-ref.txt", letters);
                i++;
            }
        }
        System.out.println("MAIN Over");
    }



    public static char[] matchCharRef(final int i) {
        switch (i) {
            case 0:
                return capitals;
            case 1:
                return lettersref;
            case 2:
                return numbers;
            case 3:
                return symbolsref;
        }
        return null;
    }

    public static void setLetters(List<ShapeModel> letters, char[] chars) {
        if (letters.size() != chars.length) {
            // WTF ?
            return ;
        }
        int i = 0;
        for (ShapeModel letter : letters) {
            letter.setC(chars[i++]);
        }
    }
}
