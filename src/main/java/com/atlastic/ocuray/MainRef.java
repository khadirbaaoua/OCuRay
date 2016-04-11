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
import java.util.ArrayList;
import java.util.List;

public class MainRef {
    static char[] capitals = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R'
    ,'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    static char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'
    ,'s', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A'};
    static char[] lettersref = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', '.', 'i', '.', 'j', 'k', 'l', 'm', 'n', 'o'
            , 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A'};
    static char[] numbers = {'1', '2', '3', '4', '5', '6', '7', '8' , '9', '0', 'A'};
    static char[] symbols = {'(', ')', '[', ']', ',', '.', '/', '°', '#', '@', '\'', '§', '-', '_', '*', '^', '$'
            , '€', '£', '`', '´', '+', '<', '>', '|', 'A'};
    static char[][] compounds = {
            {'%', '°', '1', '3'},
            {'%', '/', '2', '3'},
            {'%', '°', '3', '3'},
            {'=', '-', '1', '2'},
            {'=', '-', '2', '2'},
            {';', '.', '1', '2'},
            {';', ',', '2', '2'},
            {':', '.', '1', '2'},
            {':', '.', '2', '2'},
            {'"', '\'', '1', '2'},
            {'"', '\'', '2', '2'},
            {'!', '.', '1', '2'},
            {'!', '|', '2', '2'},
            {'à', 'a', '1', '2'},
            {'à', '`', '2', '2'},
            {'â', 'a', '1', '2'},
            {'â', '^', '2', '2'},
            {'é', 'e', '1', '2'},
            {'é', '´', '2', '2'},
            {'è', 'e', '1', '2'},
            {'è', '`', '2', '2'},
            {'ê', 'e', '1', '2'},
            {'ê', '^', '2', '2'},
            {'ï', 'i', '1', '3'},
            {'ï', '.', '2', '3'},
            {'ï', '.', '3', '3'},
            {'î', 'i', '1', '2'},
            {'î', '^', '2', '2'},
            {'ô', 'o', '1', '2'},
            {'ô', '^', '2', '2'},
            {'ü', 'u', '1', '3'},
            {'ü', '.', '2', '3'},
            {'ü', '.', '3', '3'}
    };
    static String[] refs = {"capitals-generated.png", "letters-generated.png", "numbers-generated.png",
            "symbols-generated.png"/*, "compounds.png"*/};
    static String[] fonts = {"Arial-black", "Verdana", "Courier", "Helvetica", "TrebuchetMS", "Times-Roman", "Tahoma",
            "Lucida Sans Regular", "Georgia", /*"ComicSansMS",*/ "Avenir-Black", "Serif"};
    static String path = "ref/";
    static String compoundsPath = path + "compounds-ref.txt";


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
        ImageHelper.createImageWithText(symbols, path + "symbols-generated.png", font);
    }

    public static List<String> getFonts() {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts(); // Get the fonts
        List<String> res = new ArrayList<>();
        int i = 0;
        for (Font f : fonts) {
            res.add(f.getFontName());
        }
        return res;
    }

    public static void main(String[] args) throws IOException{
        String currentPath;
        List<String> paths = new ArrayList<>();
        int i;
        List<String> fontsSystem = getFonts();
        for (String font : fonts) {
            if (!fontsSystem.contains(font)) {
                System.out.println("Skipping font[" + font + "] as it is not available in this system");
            }
            createDirectory(path, font);
            generateImageLetters(path + font + "/", font);
            i = 0;
            for (String ref : refs) {
                currentPath = path + font + "/" + ref;
                paths.add(currentPath + "-ref.txt");
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
        System.out.println("Special case: compounds, writing file = "+compoundsPath);
        generateCompoundFile(compoundsPath);
        paths.add(compoundsPath);
        System.out.println("Merging all ref files into one");
        FileHelper.mergeFilesToSingleFile(paths, path + "ref-all.db");
        System.out.println("MAIN Over");
    }

    public static void generateCompoundFile(final String path) throws IOException {
        FileHelper.writeCompoundsToFile(compounds, path);
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
                return symbols;
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
