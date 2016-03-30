package com.atlastic.ocuray.image.analysis;

import com.atlastic.ocuray.Constants;
import com.oracle.tools.packager.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khadirbaaoua on 17/03/2016.
 */
public class ShapeComparator {
    public static List<DbRef> dbReferences = new ArrayList();
    public static double averageSize = 0.0;
    public static double averageLetterHeight = 0.0;

    // euclidian distance between two sides, the more it nears the 0, the nearer the shapes are
    public static double computeEuclidianDistance(final ShapeVector v1, final ShapeVector v2) {
        double res = 0.0;
        // vectors can't be compared because they don't have the same size
        if (v1.getSize() != v2.getSize()) {
            return 1;
        }
        final int width = v1.getSize();
        for (int i = 0; i < width; i++) {
            res += Math.pow(v1.getAt(i) - v2.getAt(i), 2);
        }
        return Math.sqrt(res);
    }

    // euclidian distances between two shapes
    public static double getSimilarityBetweenSides(final ShapeVector[] v1, final ShapeVector[] v2,
                                                   final double ratioV1, final double ratioV2) {
        double res = 0.0;
        double tmp;
        for (int i = 0; i < 4; i++) {
            tmp = computeEuclidianDistance(v1[i], v2[i]);
            //System.out.println("Similarity between sides ["+i+"] : ["+tmp+"]");
            res += tmp;
        }
        // added fifth coordinate to vector (ratio)
        //System.out.println("Ratio shape [" + ratioV1 + "], ratio ref ["+ratioV2+"]");
        res += Math.sqrt(Math.pow(ratioV1 - ratioV2, 2));
        return res / 5;
    }


    public static char compareShapeWithDb(final ShapeVector[] v1, final double relativeSize, final double ratio) {
        double res = -1.0;
        char c = '∏';
        double tmp;
        if (dbReferences == null || dbReferences.size() <= 0) {
            throw new RuntimeException("Ref db is not initialized, size="+dbReferences.size());
        }
        for (DbRef dbRef : dbReferences) {
            if (dbRef.isMultipartRef()) {
                continue;
            }
            //System.out.println("Comparing to ref ["+dbRef.getC()+"]");
            tmp = getSimilarityBetweenSides(v1, dbRef.getSides(), ratio, dbRef.getRatio());
            // weight euclidian distance with size difference
            tmp /= 1 - (Math.abs(dbRef.getRelativeSize() - relativeSize));
            // and ratio
            //tmp /= 1 - (Math.abs(dbRef.getRatio() - ratio));

            //System.out.println("Result for ref ["+dbRef.getC()+","+relativeSize+ "," + ratio + "] = "+tmp);
            if (res == -1 || tmp < res) {
                //System.out.println("Setting nearest match to "+dbRef.getC());
                res = tmp;
                c = dbRef.getC();
            }
        }
        return c;
    }

    // get db ref with corresponding char
    public static DbRef getDbRefFromChar(final char c) {
        for (DbRef dbRef : dbReferences) {
            if (dbRef.getC() == c) {
                return dbRef;
            }
        }
        return null;
    }

    // load ref db
    public static void loadDbRef() throws IOException {
        String path = Constants.PATH_DB;
        System.out.println("Trying  to load db at "+path);
        File dbFile = new File(path);
        System.out.println("Full path of file : "+ dbFile.getAbsolutePath());
        if (dbFile.exists()) {
            List<String> lines = Files.readAllLines(FileSystems.getDefault().getPath(path));
            double ratio, relativeSize;
            char c;
            ShapeVector[] curRef;
            for (String line : lines) {
                // ignore comments
                if (line.startsWith("###")) {
                    continue;
                }
                String[] lineData = line.split(" ");
                c = lineData[0].charAt(0);
                if (lineData.length == 4) {
                    DbRef ref = getDbRefFromChar(lineData[1].charAt(0));
                    if (ref != null) {
                        dbReferences.add(new DbRef(c, ref, Integer.parseInt(lineData[2]),
                                Integer.parseInt(lineData[2])));
                    } else {
                        throw new IOException("Missing ref in reference file or order is wrong ?");
                    }
                } else if(lineData.length == 7) {
                    curRef = new ShapeVector[4];
                    curRef[0] = new ShapeVector(lineData[1]);
                    curRef[1] = new ShapeVector(lineData[2]);
                    curRef[2] = new ShapeVector(lineData[3]);
                    curRef[3] = new ShapeVector(lineData[4]);
                    ratio = Double.parseDouble(lineData[5]);
                    relativeSize = Double.parseDouble(lineData[6]);
                    dbReferences.add(new DbRef(c, ratio, relativeSize, curRef));
                }
            }
        } else {
            System.out.println("The db file does not exists");
        }
    }

}
