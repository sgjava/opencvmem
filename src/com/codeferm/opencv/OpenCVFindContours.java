/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on May 22, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.opencv;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_8UC1;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * This program will show how Imgproc.findContours leaks memory.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
public class OpenCVFindContours {
    /* Load the OpenCV system library */

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Kernel used for contours.
     */
    private static final Mat CONTOUR_KERNEL = Imgproc.getStructuringElement(
            Imgproc.MORPH_DILATE, new Size(3, 3), new Point(1, 1));

    /**
     * Create Mat in a loop.
     *
     * @param args The command line arguments.
     * @throws InterruptedException Possible exception.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Calling Imgproc.findContours");
        final Mat mat = new Mat(480, 640, CV_8UC1);
        // Use a rect to get hit on findContours
        final Rect rect = new Rect(10, 10, 100, 100);
        Imgproc.
                rectangle(mat, rect.tl(), rect.br(), new Scalar(255, 255, 255),
                        2);
        // Contour hierarchy.
        final Mat hierarchy = new Mat();
        // Do it 100 times, so it shows up more easily in Valgrind
        for (int i = 0; i < 100; i++) {
            final List<MatOfPoint> contoursList = new ArrayList<MatOfPoint>();
            Imgproc.findContours(mat, contoursList, hierarchy,
                    Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
            // Free contours
            for (MatOfPoint contours : contoursList) {
                contours.free();
            }
        }
        hierarchy.free();
        mat.free();
        // See if we should explicitly garbage collect (you should never do this)
        // http://stackoverflow.com/questions/2414105/why-is-it-bad-practice-to-call-system-gc
        if (args.length > 0 && args[0].toUpperCase().equals("GC")) {
            System.out.println("Calling System.gc()");
            System.gc();
        }
        System.out.println("Sleeping");
        // You can use this time to view memory usage
        Thread.sleep(10000);
        System.out.println("Done");
    }

}
