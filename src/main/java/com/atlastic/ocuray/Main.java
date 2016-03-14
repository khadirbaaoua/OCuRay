package com.atlastic.ocuray;

import com.atlastic.ocuray.image.analysis.ShapeReader;
import com.atlastic.ocuray.image.analysis.ShapeUtils;
import com.atlastic.ocuray.image.filter.ContrastFilter;
import com.atlastic.ocuray.image.filter.NoiseFilter;
import com.atlastic.ocuray.image.tool.FileHelper;
import com.atlastic.ocuray.image.tool.ImageHelper;

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
        /*for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                System.out.print(pixels[i][j] == 255 ? "-" : "*");
            }
            System.out.println("   ");
        }*/
        List<List<Point>> shapes = ShapeReader.readShapes(pixels);
        System.out.println("Shape reading done, "+shapes.size()+" shapes red");
        FileHelper.writeShapesToImage(pixels.length, pixels[0].length, args[0] + "-shapes.png", shapes, pixels);
        System.out.println("Converting shapes to outline");
        List<List<Point>> shapesOutline = ShapeUtils.getOutlineFromShapes(pixels, shapes);
        System.out.println("Shapes successfully converted to outline, writing to file");
        FileHelper.writeShapesToImage(pixels.length, pixels[0].length, args[0] + "-shapes-outline.png", shapesOutline,
                pixels);
        System.out.println("MAIN Over");
    }
}
