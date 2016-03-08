package com.atlastic.ocuray;

import com.atlastic.ocuray.image.filter.ContrastFilter;
import com.atlastic.ocuray.image.filter.NoiseFilter;
import com.atlastic.ocuray.image.tool.FileHelper;
import com.atlastic.ocuray.image.tool.ImageHelper;

import java.awt.image.BufferedImage;
import java.io.IOException;

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
        //FileHelper.writeArrayToFile(pixels, "gray-pixels.txt");
    }
}
