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
import java.awt.image.BufferedImage;

/**
 * An interface for {@code FaceRecognitionDevice} listener.
 * 
 * Please remember that each servo can move from 0° to 180°. 
 *
 * @author Gabriele Tafuro
 *
 * @see FaceRecognitionDevice
 *
 * @since 1.0
 */
public interface FaceRecognitionDeviceListener {
  /**
   * It signals to the listener that a new face-recognition has occurred.
   * 
   * @param point
   *        The point of the centre of the rectangle drawn from the
   *        face-recognition device.
   * 
   * @param dimension
   *        The width and height of the area supervised by the face-recognition
   *        device.
   *        
   * @param image 
   *        The image sent from the face-recognition device.
   */
  public void actionPerformed(
          it.gftblues.sixdofarm.Point point,
          Dimension dimension,
          BufferedImage image
  );
}
