package it.gftblues.sixdofarm.ui;

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
import it.gftblues.sixdofarm.controllers.SerialCommunicator;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

/**
 * Interface for the {@code SixDOFArmController}.
 * 
 * @author Gabriele Tafuro
 *
 * @since 1.0
 */
public interface SixDOFArmControllerUI {

  /**
   * Provide to the Pointer application the dimensions (width and height) in 
   * pixels of a surface where the pointing device (mouse, stick, trackball, 
   * etc.) moves in in order to map the movement in to degrees (0-180°).
   * The bigger the surface the better the Pointer precision.
   *
   * @return a
   *         {@code Dimension} object with width and height.
   */
  public Dimension getTargetDimension();

  /**
   * Ask the UI to deal with a message that the Pointer has to send to the 
   * utilizer.
   * 
   * @param message
   *        The message to be dealt with.
   */
  public void sendFeedback(String message);
  
  /**
   * Ask the UI to deal with an error has occurred and that the Pointer has to 
   * notify to the utilizer.
   * 
   * @param error
   *        The error message to be dealt with.
   */
  public void showError(String error);

  public void showMessage(String message);
  
  public void showWarningMessage(String xostrMessage);
  
  public void notifyProgramMessage(Level level, String message);
  public void notifyProgramMessage(Level level, String message, Object param);
  
  /**
   * Gets the serial port name.
   * 
   * @return The serial port name.
   */
  public String getSerialPortName();
  
  /**
   * Sets the serial port name.
   * 
   * @param val
   *        The serial port name.
   */
  public void setSerialPortName(String val);

  /**
   * Sets the full pathname of the log file.
   * @param pathname
   *        The full pathname of the log file.
   */
  public void setLogFilePathname(String pathname);  

  /**
   * Gets the serial ports data-rate.
   * 
   * @return return the selected data-rate of the serial port.
   */
  public int getSerialDataRate();
  
  /**
   * Sets the serial port data-rate.
   * 
   * @param val 
   *        The serial port data-rate.
   */
  public void setSerialDataRate(int val);
  
  /**
   * Gets the number of data-bit for the serial port.
   * 
   * @return The serial port data-bit.
   */
  public int getSerialDataBit();
  
  /**
   * Sets the number of data-bit for the serial port.
   * 
   * @param val
   *        The serial port data-bit.
   */
  public void setSerialDataBit(int val);
 
  /**
   * Gets the parity for the serial port.
   * 
   * @return The parity of the serial port.
   */
  public SerialCommunicator.Parity getSerialParity();
  
  /**
   * Sets the parity for the serial port.
   * 
   * @param val The parity of the serial port.
   */
  public void setSerialParity(SerialCommunicator.Parity val);
  
  /**
   * Gets the stop-bit for the serial port.
   * 
   * @return The stop-bit of the serial port.
   */
  public int getSerialStopBit();
  
  /**
   * Sets the stop-bit for the serial port.
   * 
   * @param val
   *        The stop-bit of the serial port.
   */
  public void setSerialStopBit(int val);
  
  /**
   * Gets the timeout for the serial port.
   * 
   * @return The timeout of the serial port.
   */
  public int getSerialTimeout();
  
  /**
   * Sets the timeout for the serial port.
   * 
   * @param val
   *        The timeout of the serial port.
   */
  public void setSerialTimeout(int val);
  
  /**
   * Sets the name for the serial port.
   * 
   * @param names
   *        An array with the serial ports names.
   */
  public void setSerialPortNames(String[] names);
  
  /**
   * Sets the possible data-rates.
   * 
   * @param names
   *        An array with the possible data-rates for the serial port.
   */
  public void setSerialDataRates(String[] names);
  
  /**
   * Sets the possible data-bits.
   * 
   * @param names
   *        An array with the possible data-bits for the serial port.
   */
  public void setSerialDataBits(String[] names);
  
  /**
   * Set the possible parities.
   * 
   * @param names
   *        An array with possible parities for the serial port.
   */
  public void setSerialParities(String[] names);

  /**
   * Sets the possible stop-bits.
   * 
   * @param names 
   *        An array with the possible stop-bits for the serial port.
   */
  public void setSerialStopBits(String[] names);
  
  /**
   * Sets the video-frame from the {@code FaceRecognitionDevice}.
   * 
   * @param frame
   *        The frame (image) from the {@code FaceRecognitionDevice}.
   */
  public void setVideoFrame(BufferedImage frame);
  
  /**
   * Signals to the UI that the video stream from 
   * the {@code FaceRecognitionDevice} has stopped.
   */
  public void videoHasStopped();
  
  /**
   * Provides the user's preference on the use of the 
   * {@code FaceRecognitionDevice}.
   * 
   * @return {@code true} if the face-recognition device has to be used.
   *         {@code false} otherwise.
   */
  public boolean useFaceRecognition();
  
  /**
   * Sets the use of the {@code FaceRecognitionDevice}.
   * @param use
   *        {@code true} for using the face-recognition device, {@code false}
   *        otherwise.
   * 
   */
  public void setUseFaceRecognition(boolean use);

  /**
   * Gets the path of the classification file.
   * 
   * @return The classifier file-path.
   */
  public String getClassifierFilename();
  
  /**
   * Sets the path of the classification file.
   * 
   * @param classifierFilename
   *        The classifier file-path.
   */
  public void setClassifierFilename(String classifierFilename);

  /**
   * Provides the users preference on the use of game controllers.
   * 
   * @return {@code true} if game controllers have to be used, {@code false}
   *         otherwise.
   */
  public boolean isGameControllerEnabled();
  
  /**
   * Sets the use of game controllers.
   * 
   * @param enable
   *        {@code true} of game controllers have to be used, {@code false}
   *        otherwise.
   */
  public void setGameCOntrollerEnabled(boolean enable);
  
  /**
   * Sets the gravity value for the Maths panel.
   * 
   * @param gravity 
   *        The value of gravity.  If used on Earth it should be 9.807.
   *        Check the right one according to the celestial body you are using
   *        this application. :)
   */
  public void setGravity(double gravity);

  public void setShoulderMaths(double mass, double length, double torque);
  public void setElbowMaths(double mass, double length, double torque);
  public void setWristMaths(double mass, double length, double torque);

  public void setShoulderVerticalPosition(int deg);
  public void setElbowVerticalPosition(int deg);
  public void setWristVerticalPosition(int deg);
  public void setWristRotationalPosition(int deg);
  public void setShoulderHorizontalPosition(int deg);
  public void setClampAperturelPosition(int deg);

  public void setClampLength(double len);
  public void setElbowLength(double len);
  public void setShoulderLength(double len);
  public void setBaseHeight(double len);
  
  public void setTimeBetweenCommands(int milliseconds);
  
  /**
   * Gets the pathname of the program loaded into the program tab.
   * @return The program pathname or {@code null} if not defined.
   */
  public String getProgramPathname();
}
