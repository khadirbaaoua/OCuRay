package com.atlastic.ocuray.image.analysis;

import com.atlastic.ocuray.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
            return 10;
        }
        final int width = v1.getSize();
        for (int i = 0; i < width; i++) {
            res += Math.pow(v1.getAt(i) - v2.getAt(i), 2);
        }
        return Math.sqrt(res);
    }

    // euclidian distances between a shape and a ref
    public static double getSimilarityBetweenSides(final ShapeModel v1, final DbRef v2) {
        double res = 0.0;
        double tmp;
        // compare sides vectors
        for (int i = 0; i < 5; i++) {
            tmp = computeEuclidianDistance(v1.getVectors()[i], v2.getSides()[i]);
            //System.out.println("Similarity between sides ["+i+"] : ["+tmp+"]");
            res += tmp;
        }
        // compute another vector distance (relative size, ratio and occupation)
        tmp = Math.pow(v1.getRatio() - v2.getRatio(), 2);
        tmp += Math.pow(v1.getRelativeSize() - v2.getRelativeSize(), 2);
        tmp += Math.pow(v1.getOccupation() - v2.getOccupation(), 2);
        tmp = Math.sqrt(tmp);
        return (res / 5.0) + tmp;
    }

    // double matching : size and negativity
    public static boolean matchSides(final ShapeVector[] v, final ShapeVector[] ref) {
        for (int i = 0; i < 5; i++) {
            if (v[i].getSize() != ref[i].getSize()
                    || !ShapeMaths.matchDoublesSigns(v[i].getAll(), ref[i].getAll())) {
                return false;
            }
        }
        return true;
    }

    public static char compareShapeWithDb(final ShapeModel v1) {
        double res = -1.0;
        char c = '∏';
        double tmp, tmp2;
        if (dbReferences == null || dbReferences.size() <= 0) {
            throw new RuntimeException("Ref db is not initialized, size="+dbReferences.size());
        }
        System.out.println("Relative size of shape : "+v1.getRelativeSize());
        /*System.out.println("Sides size");
        for (ShapeVector v : v1) {
            System.out.print(" "+v.getSize());
        }
        System.out.println();*/
        int lineMatched = -1;
        for (DbRef dbRef : dbReferences) {
            if (dbRef.isMultipartRef()) {
                continue;
            }
            // if shape's number or sides don't match, skip
            /*if (!matchSides(v1.getVectors(), dbRef.getSides())) {
                //System.out.println("Skipping c["+dbRef.getC()+"], sides are different");
                continue;
            }*/
            //System.out.println("Comparing to ref ["+dbRef.getC()+"]");
            tmp = getSimilarityBetweenSides(v1, dbRef);
            // weight euclidian distance with ration difference
            //System.out.println("Result for ref ["+dbRef.getC()+","+relativeSize+ "," + ratio + "] = "+tmp+" // "+tmp2);
            if (res == -1 || tmp < res) {
                //System.out.println("Setting nearest match to "+dbRef.getC());
                res = tmp;
                c = dbRef.getC();
                lineMatched = dbRef.getLineNumber();
            }
        }
        System.out.println("Line matched : "+lineMatched);
        return c;
    }

    // get db ref with corresponding char
    public static DbRef getDbRefFromChar(final char c) {
        for (DbRef dbRef : dbReferences) {
            if (dbRef.getC() == c && !dbRef.isMultipartRef()) {
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
        int i = 0;
        if (dbFile.exists()) {
            List<String> lines = Files.readAllLines(FileSystems.getDefault().getPath(path));
            double ratio, relativeSize, occupation;
            char c;
            ShapeVector[] curRef;
            for (String line : lines) {
                // ignore comments
                i++;
                if (line.startsWith("###")) {
                    continue;
                }
                String[] lineData = line.split(" ");
                c = lineData[0].charAt(0);
                if (lineData.length == 4) {
                    c = lineData[1].charAt(0);
                    DbRef ref = getDbRefFromChar(c);
                    if (ref != null) {
                        dbReferences.add(new DbRef(lineData[0].charAt(0), ref, Integer.parseInt(lineData[2]),
                                Integer.parseInt(lineData[3]), i, -1));
                    } else {
                        throw new IOException("Missing ref in reference file or order is wrong ? "
                        + "Line number [" + i+"] for compound [" + c + "]");
                    }
                } else if(lineData.length == 10) {
                    curRef = new ShapeVector[6];
                    curRef[0] = new ShapeVector(lineData[1]);
                    curRef[1] = new ShapeVector(lineData[2]);
                    curRef[2] = new ShapeVector(lineData[3]);
                    curRef[3] = new ShapeVector(lineData[4]);
                    curRef[4] = new ShapeVector(lineData[5]);
                    curRef[5] = new ShapeVector(lineData[6]);
                    ratio = Double.parseDouble(lineData[7]);
                    relativeSize = Double.parseDouble(lineData[8]);
                    occupation = Double.parseDouble(lineData[9]);
                    dbReferences.add(new DbRef(c, ratio, relativeSize, curRef, i, occupation));
                }
            }
        } else {
            System.out.println("The db file does not exists");
        }
    }

}
