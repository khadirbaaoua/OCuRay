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
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException{
	    if (args.length != 1) {
            System.out.print("Usage : ./ocuray path-to-image");
            return;
        }
        // Load image
        System.out.println("Trying to read img " + args[0]);
        BufferedImage img = ImageHelper.loadImageFromPath(args[0]);
        short[][] pixels = ImageHelper.convertTo2DAndGrayscaleUsingRGB(img);
        FileHelper.writePixelsToImage(pixels, "converted-to-grayscale.png");
        ContrastFilter.doFilter(pixels);
        FileHelper.writePixelsToImage(pixels, "grayscale-contrasted.png");
        NoiseFilter.doFilter(pixels);
        FileHelper.writePixelsToImage(pixels, "grayscale-contrasted-noiseless.png");
        System.out.println("Image filtering done, reading shapes");
        List<List<Point>> shapes = ShapeReader.readShapes(pixels);
        System.out.println("Shape reading done, "+shapes.size()+" shapes red");
        FileHelper.writeShapesToImage(pixels.length, pixels[0].length, args[0] + "-shapes.png", shapes, pixels);
        System.out.println("Converting shapes to outline");
        List<List<Point>> shapesOutline = ShapeUtils.getOutlineFromShapes(pixels, shapes);
        System.out.println("Shapes successfully converted to outline, writing to file");
        FileHelper.writeShapesToImage(pixels.length, pixels[0].length, args[0] + "-shapes-outline.png", shapesOutline,
                pixels);
        try {
            System.out.println("Loading DB ref");
            ShapeComparator.loadDbRef();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            return ;
        }
        System.out.println("Analyzing shape outline");
        List<ShapeModel> letters = ShapeAnalysis.analyzeShapes(shapes);
        System.out.println("Analyzing letters and extracting words");
        List<Line> lines = ShapeSemantics.getSemanticsOutOfShapes(letters);
        System.out.println("Displaying lines and words");
        for (Line line : lines) {
            System.out.println("Line : [" + line.toStr() + "]");
        }
        System.out.println("Semantic analysis");
        Document doc = TextAnalysis.doTheStuff(lines);
        System.out.println(doc.toString());
        System.out.println("MAIN Over");
    }
}
