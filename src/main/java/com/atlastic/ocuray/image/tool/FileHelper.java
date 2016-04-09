package com.atlastic.ocuray.image.tool;

import com.atlastic.ocuray.image.analysis.ShapeModel;
import com.atlastic.ocuray.image.analysis.ShapeVector;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.List;
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
        BufferedImage res = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_BYTE_GRAY);
        Color c;
        File output = new File(path);
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                c = new Color(pixels[i][j], pixels[i][j], pixels[i][j]);
                res.setRGB(j, i, c.getRGB());

            }
        }
        ImageIO.write(res, "png", output);
    }

    public static void writeShapesToImage(final int width, final int height, final String path,
                                          final List<List<Point>> shapes, final short[][] pixels) throws IOException {
        BufferedImage res = new BufferedImage(height, width, BufferedImage.TYPE_BYTE_GRAY);
        File output = new File(path);
        shapes.forEach(shape ->
                        shape.forEach(point -> {
                            final Color c = new Color(255, 255, 255);
                            res.setRGB(point.y, point.x, c.getRGB());
                        })
        );
        ImageIO.write(res, "png", output);
    }

    public static void writeShapeModelToFile(final String path, final List<ShapeModel> letters)
            throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        Writer writer = new BufferedWriter(fw);
        StringBuffer sb = new StringBuffer();
        double[] cur;
        for (ShapeModel letter : letters) {
            sb.append(letter.getC()).append(" ");
            // vectors
            for (ShapeVector v : letter.getVectors()) {
                cur = v.getAll();
                for (int i = 0; i < cur.length; i++) {
                    sb.append(cur[i]);
                    if (i != cur.length - 1) {
                        sb.append(",");
                    }
                }
                sb.append(" ");
            }
            sb.append(letter.getRatio()).append(" ").append(letter.getRelativeSize()).append("\n");
        }
        writer.write(sb.toString());
        writer.close();
        System.out.println("Wrote the string : \n" + sb.toString());
    }

    private static void joinFiles(File destination, File[] sources)
            throws IOException {
        OutputStream output = null;
        try {
            output = createAppendableStream(destination);
            for (File source : sources) {
                appendFile(output, source);
            }
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    private static BufferedOutputStream createAppendableStream(File destination)
            throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(destination, true));
    }

    private static void appendFile(OutputStream output, File source)
            throws IOException {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(source));
            IOUtils.copy(input, output);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    public static void mergeFilesToSingleFile(final List<String> paths, final String finalPath) {
        File[] files = new File[paths.size()];
        int i = 0;
        for (String path : paths) {
            files[i++] = new File(path);
        }
        File finalFile = new File(finalPath);
        try {
            joinFiles(finalFile, files);
        } catch (IOException e) {
            System.out.println("Error while merging ref files : "+e.getMessage());
        }
    }
}