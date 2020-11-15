package it.gftblues.sixdofarm.controllers;

/**
 * Copyright 2020 Gabriele Tafuro
 * 
 * This file is part of Pierin-oh!.
 *
 * Pierin-oh! is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  Pierin-oh! is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import it.gftblues.sixdofarm.Dimension;
import it.gftblues.utils.FileUtils;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class based on OpenCV that uses the PC-cam to follow the user's face.
 *
 * @author Gabriele Tafuro
 *
 *
 * @since 1.0
 */

public class FaceRecognitionDevice {
  
  /**
   * The OpenCV classifier filename.
   */
  private String classifierName;

  private Mat frame;
  private VideoCapture camera;
  private CascadeClassifier faceDetector;
  private Thread recognition = null;
  
  private final List<FaceRecognitionDeviceListener> listeners = 
          new ArrayList<>();

  private final AtomicBoolean running = new AtomicBoolean(false);

  /**
   * Constructor.
   * 
   * @param classifierName
   *        The OpenCV classifier filename.
   */
  public FaceRecognitionDevice(String classifierName) {
    String osPlatform = System.getProperty("os.name"); 
//    System.out.println(osPlatform);
    try {
      if (osPlatform.contains("Windows")) {
        FileUtils.loadJarDll("/lib/opencv_java440.dll");
//      System.load("/lib/opencv_java440.dll");
//      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      } else if (osPlatform.contains("Linux")) {
        FileUtils.loadJarDll("/lib/libopencv_java440.so");
      } else {
        Logger.getLogger(FaceRecognitionDevice.class.getName()).log(
                Level.SEVERE, 
                null, 
                String.format("Platform %s not supported.",osPlatform)
        );
      }
    } catch (IOException ex) {
        Logger.getLogger(
                FaceRecognitionDevice.class.getName()).log(
                        Level.SEVERE,
                        null,
                        ex
        );
    }
    this.classifierName = classifierName;
  }

  /**
   * Adds a {@code FaceRecognitionDeviceListener}.
   * @param listener 
   *        The {@code FaceRecognitionDeviceListener}.
   */
  public void addFaceRecognitionDeviceListener(
          FaceRecognitionDeviceListener listener
  ) {
    listeners.add(listener);
  }
  
  /**
   * Removes a {@code FaceRecognitionDeviceListener}.
   * @param listener 
   *        The {@code FaceRecognitionDeviceListener}.
   */
  public void removeFaceRecognitionDeviceListener(
          FaceRecognitionDeviceListener listener
  ) {
    listeners.remove(listener);
  }

  /**
   * Starts the recognition activity.
   */
  public void startRecognition() {
    frame = new Mat();
    camera = new VideoCapture(0);
    faceDetector = new CascadeClassifier();

    if (faceDetector.load(classifierName)) {
      recognition = new Thread(() -> {
        while (true) {
          if (camera != null && camera.isOpened() && camera.read(frame)) {
            MatOfRect faceDetections = new MatOfRect();
//            System.out.println("frame: "+frame);
            faceDetector.detectMultiScale(frame, faceDetections);
/*            System.out.println(
                    String.format(
                            "Detected %s faces", 
                            faceDetections.toArray().length
                    )
            );*/
            Rect best = new Rect();
            for (Rect rect : faceDetections.toArray()) {
              if (rect.width > best.width) {
                best = rect;
              }
            }
            Imgproc.rectangle(
                    frame, 
                    new Point(best.x, best.y), 
                    new Point(best.x+best.width, best.y+best.height), 
                    new Scalar(0, 255, 0)
            );
            
            // scales the input image to the output image
            BufferedImage inputImage = mat2BufferedImage(frame);
            if (inputImage != null) {
              if (!listeners.isEmpty()) {
                ListIterator<FaceRecognitionDeviceListener> iter = 
                        listeners.listIterator();
                while(iter.hasNext()) {
                  iter.next().actionPerformed(new it.gftblues.sixdofarm.Point(best.x, best.y),
                          new Dimension(best.width, best.height),
                          inputImage
                  );
                }
              }
            }
          }
        }
      });
      recognition.start();
      running.set(true);
    }
  }

  /**
   * Stops the recognition activity.
   */
  public void stopRecognition() {
    if (running.get()) {
      recognition.interrupt();
      running.set(false);
      camera.release();
      camera = null;
    }
    recognition = null;
  }

  /**
   * Check whether the recognition process is running.
   * @return Returns {@code true} if the recognition process is running, 
   * {@code false} otherwise.
   */
  public boolean isRunning() {
    return running.get();
  }

  /**
   * Gets the classifier filename.
   * @return The classifier filename.
   */
  public String getClassifierName() {
    return classifierName;
  }

  /**
   * Sets the classifier filename.
   * @param classifierName The classifier filename.
   */
  public void setClassifierName(String classifierName) {
    this.classifierName = classifierName;
  }

  /**
   * Gets a frame caputerd from the camera. 
   * @param m The {@code Mat} obejct from which the image is estrapolated.
   * @return A {@code BufferedImage} with the image.
   */
  private static BufferedImage mat2BufferedImage(Mat m) {
    int type = BufferedImage.TYPE_BYTE_GRAY;
    if (m.channels() > 1) {
        type = BufferedImage.TYPE_3BYTE_BGR;
    }
    int bufferSize = m.channels()*m.cols()*m.rows();
    byte [] b = new byte[bufferSize];
    m.get(0,0,b); // get all the pixels
    if (m.rows() != 0 && m.cols() != 0) {
      BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
      WritableRaster wr = image.getRaster();
      if (wr != null) {
        DataBuffer db = wr.getDataBuffer();
        if (db != null && db instanceof DataBufferByte) {
          final byte[] targetPixels = ((DataBufferByte)db).getData();
          System.arraycopy(b, 0, targetPixels, 0, b.length);  
        } else {
          System.out.println("FaceRecognitionDevice::mat2BufferedImage()"+
                  "Somethig bad is going on.");
        }
      }
      return image;
    }
    return null;
  }
}
