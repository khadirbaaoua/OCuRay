package com.atlastic.ocuray.image.tool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

/*
   Helper class for Image manipulation
 */
public class ImageHelper {
    public static short brightness = 80;
    public static BufferedImage loadImageFromPath(final String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.print("Could not load path [" + path + "], reason : " + e.getMessage());
        }
        return img;
    }

    public static BufferedImage convertToGrayScaleFromBufferedImage(final BufferedImage biColor) {
        BufferedImage biGray = new BufferedImage(biColor.getWidth(), biColor.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        biGray.getGraphics().drawImage(biColor, 0, 0, null);
        return biGray;
    }

    public static void convertToGrayScaleFromBytes(byte[] imageData, int width, int height) throws IOException {

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        byte [] newData = ((DataBufferByte) newImage.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < imageData.length; i++)
        {
            newData[i] = imageData[i];
        }
        System.out.println("Writing image");
        FileHelper.writeImageToFile("grayscale.png", newImage);
    }

    public static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                System.out.println("Pixels (aRGB) : " + pixels[pixel] + ", " + pixels[pixel + 1] + ", " + pixels[pixel + 2] + ", " + pixels[pixel + 3]);
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                System.out.println("Pixels (RGB) : " + pixels[pixel] + ", " + pixels[pixel + 1] + ", " + pixels[pixel + 2]);
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    public static int adjustedColor(final int color) {
        int adjusted = color + brightness;
        return (adjusted > 255 ? 255 : adjusted);
    }

    public static short[][] convertTo2DAndGrayscaleUsingRGB(final BufferedImage image) throws IOException {
        if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0) {
            throw new IOException("Image is null or has no content in it");
        }

        int width = image.getWidth();
        int height = image.getHeight();
        short[][] res = new short[height][width];
        int count = 0;
        //System.out.println("width: " + width);
        //System.out.println("height: " + height);
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                count++;
                Color c = new Color(image.getRGB(j, i));
                //System.out.println("i ["+i+"], j ["+j+"], color : "+((c.getRed() + c.getGreen() +  c.getBlue()) / 3));
                // luminosity algorithm : 0.21 R + 0.72 G + 0.07 B.
                res[i][j] = (short)Math.round(0.21 * adjustedColor(c.getRed()) + 0.72 * adjustedColor(c.getGreen())
                        + 0.07 * adjustedColor(c.getBlue()));
                // average method : (r + g + b) / 3
                //res[i][j] = (short) ((c.getRed() + c.getGreen() +  c.getBlue()) / 3);
                // lightness (max(R, G, B) + min(R, G, B)) / 2
                //res[i][j] = (max(c.getRed(), c.getBlue(), c.getGreen()) + min(c.getRed(), c.getBlue(), c.getGreen())) / 2
            }
        }
        return res;
    }
}
