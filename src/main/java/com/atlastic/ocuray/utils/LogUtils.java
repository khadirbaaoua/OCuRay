package com.atlastic.ocuray.utils;

/**
 * Created by khadirbaaoua on 20/03/2016.
 */
public class LogUtils {
    public static void log(Object ... args) {
        if (args != null && args.length >= 1) {
            StringBuffer buf = new StringBuffer();
            for (Object obj : args) {
                buf.append(obj).append(obj).append(",");
            }
            System.out.println(buf.toString());
        }
    }
}
