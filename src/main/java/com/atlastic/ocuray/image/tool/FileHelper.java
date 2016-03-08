package com.atlastic.ocuray.image.tool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by khadirbaaoua on 12/01/2016.
 */
public class FileHelper {
    public static void writeImageToFile(final String path, BufferedImage image) throws IOException {
        File output = new File(path);
        System.out.println("Writing to file " + path);
        ImageIO.write(image, "png", output);
        System.out.println("Wrote image");
    }

    public static void writeArrayToFile(final int[][] array, final String path) throws IOException {
        File output = new File(path);
        Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path), "utf-8"));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                sb.append(array[i][j]);
            }
            sb.append("\n");
        }
        System.out.println("Trying to write int array to file");
        writer.write(sb.toString());
        System.out.println("Wrote the array");
    }

    public static void writePixelsToImage(final short[][] pixels, final String path) throws IOException {
        BufferedImage res = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_BYTE_GRAY);
        Color c;
        File output = new File(path);
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                c = new Color(pixels[i][j], pixels[i][j], pixels[i][j]);
                res.setRGB(i, j, c.getRGB());
            }
        }
        ImageIO.write(res, "png", output);
    }
}