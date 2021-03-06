/*
 * Copyright (c) Steven P. Goldsmith. All rights reserved.
 *
 * Created by Steven P. Goldsmith on May 22, 2015
 * sgoldsmith@codeferm.com
 */
package com.codeferm.opencv;

import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_8UC3;
import org.opencv.core.Mat;

/**
 * This program will create new Mat and Mat.free() to show how Finalizers
 * are not used.
 *
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
public class OpenCVFree {
    /* Load the OpenCV system library */

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Create Mat in a loop.
     *
     * @param args The command line arguments.
     * @throws InterruptedException Possible exception.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Creating Mats");
        for (int i = 0; i < 1000; i++) {
            final Mat mat = new Mat(480, 640, CV_8UC3);
            mat.free();
        }
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
        // We will call Mat.free() a few times to prove it does not crash JVM
        final Mat mat = new Mat(480, 640, CV_8UC3);
        mat.free();
        mat.free();
        mat.free();
    }

}
