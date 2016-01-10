package com.atlastic.ocuray;

import com.atlastic.ocuray.image.tool.ImageHelper;

import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
	    if (args.length != 1) {
            System.out.print("Usage : ./ocuray path-to-image");
            return;
        }
        BufferedImage img = ImageHelper.loadImageFromPath(args[0]);
        System.out.print(img.toString());
        int[][] imgBytes = ImageHelper.convertTo2DWithoutUsingGetRGB(img);
/*        for (int i = 0; i < imgBytes.length; i++) {
            for (int j = 0; j < imgBytes[i].length; j++) {
                System.out.print(imgBytes[i][j] + " - ");
            }
            System.out.println(" % ");
        }*/
    }
}
